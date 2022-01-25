import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.TransactionsTable;
import Database.mainClasses.Company;
import Database.mainClasses.Individual;
import Database.mainClasses.Transaction;
import Exceptions.InsufficientBalanceException;
import ServletHelper.ServletHelper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.naming.spi.NamingManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet(name = "TransactionsAPI", value = "/TransactionsAPI")
public class TransactionsAPI extends HttpServlet {
    ServletHelper helper = new ServletHelper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonOut = new JSONObject();
        Gson gson = new Gson();
        Date start_date = null;
        Date end_date = null;
        String as = (String) request.getParameter("as");
        String type = (String) request.getParameter("type");
        TransactionsTable tTable = new TransactionsTable();
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        int merchant;
        if(as.equals("individual")){merchant=0;}else{merchant=1;}


        try{
            if(request.getParameter("start") != null) {
                start_date = Date.valueOf(request.getParameter("start"));
                end_date = Date.valueOf(request.getParameter("end"));

                transactions = tTable.getTrans(user_id, start_date, end_date, merchant);
                for(Transaction transaction : transactions) {
                    jsonOut.append("transaction", gson.toJson(transaction, Transaction.class));
                }
                helper.createResponse(response, 200, jsonOut.toString());
            }
            else if(type != null) {
                transactions = tTable.getTrans(user_id, type, merchant);
                for (Transaction transaction : transactions) {
                    jsonOut.append("transaction", gson.toJson(transaction, Transaction.class));
                }
                helper.createResponse(response, 200, jsonOut.toString());
            }
            else {
                transactions = tTable.getTrans(user_id, merchant);
                for (Transaction transaction : transactions) {
                    jsonOut.append("transaction", gson.toJson(transaction, Transaction.class));
                }
                helper.createResponse(response, 200, jsonOut.toString());
            }
        }
        catch(Exception ex) {
            helper.createResponse(response, 403, "");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        int merchant_id = Integer.parseInt((String) jsonIn.get("merchant_id"));
        int account_id = (int) jsonIn.get("account_id");
        String username = (String) jsonIn.get("username");
        String password = (String) jsonIn.get("password");
        double amount = Double.parseDouble((String) jsonIn.get("amount"));
        int company_id = (int) jsonIn.get("as_company");
        IndividualTable iTable;
        CompanyTable cTable;
        int type;
        TransactionsTable tTable = new TransactionsTable();

        try {
            if(company_id == 0) {
                iTable = new IndividualTable();
                type = iTable.buy(account_id, merchant_id, amount);
                if(type == -1) {
                    helper.createResponse(response, 403, "Your billing limit has been reached");
                    return;
                }
                else if(type == 0)
                    tTable.insertTransaction(account_id, merchant_id, amount, "billing");
                else
                    tTable.insertTransaction(account_id, merchant_id, amount, "credit");

            }
            else {
                cTable = new CompanyTable();
                type = cTable.buy(company_id, merchant_id, amount);
                if(type == -1) {
                    helper.createResponse(response, 403, "Your billing limit has been reached");
                    return;
                }
                else if(type == 0)
                    tTable.insertTransaction(company_id, merchant_id, amount, "billing");
                else
                    tTable.insertTransaction(company_id, merchant_id, amount, "credit");
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            helper.createResponse(response, 403, e.getMessage());
            return;
        }
        helper.createResponse(response, 200, "Transaction added successfully");
    }


    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        int user_id = jsonIn.getInt("user_id");
        String as = (String) jsonIn.get("as");
        double amount = Double.parseDouble((String) jsonIn.get("amount"));

        try{
            if(as.equals("individual")) {
                IndividualTable iTable = new IndividualTable();
                Individual individual = iTable.findAccount(user_id);
                double ind_amount = individual.getRemaining_amount();
                if(ind_amount == 0) {
                    helper.createResponse(response, 403, "broke ass boy");
                    return;
                }
                else if(ind_amount < amount) {
                    iTable.payDebt(individual.getAccount_id(), ind_amount);
                    helper.createResponse(response, 200, "You payed as much of your debt as your balance " +
                            "allowed.");
                    return;
                }
                else {
                    iTable.payDebt(individual.getAccount_id(), amount);
                    helper.createResponse(response, 200, "Whole or part of the debt payed successfully.");
                }
            }
            else {
                CompanyTable cTable = new CompanyTable();
                int cid = Integer.parseInt(as);
                Company company = cTable.findAccount(cid);
                double ind_amount = company.getRemaining_amount();
                if(ind_amount == 0) {
                    helper.createResponse(response, 403, "broke ass boy");
                    return;
                }
                else if(ind_amount < amount) {
                    cTable.payDebt(company.getAccount_id(), ind_amount);
                    helper.createResponse(response, 200, "You payed as much of your debt as your balance " +
                            "allowed.");
                    return;
                }
                else {
                    cTable.payDebt(company.getAccount_id(), amount);
                    helper.createResponse(response, 200, "Whole or part of the debt payed successfully.");
                    return;
                }
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int tid = Integer.parseInt(request.getParameter("transaction_id"));
        TransactionsTable tTable = new TransactionsTable();
        try {
            tTable.refund(tid);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        helper.createResponse(response, 200, "Transaction refunded successfully");
    }
}
