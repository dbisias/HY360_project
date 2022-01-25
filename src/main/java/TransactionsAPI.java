import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.TransactionsTable;
import Database.mainClasses.Individual;
import Exceptions.InsufficientBalanceException;
import ServletHelper.ServletHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.naming.spi.NamingManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "TransactionsAPI", value = "/TransactionsAPI")
public class TransactionsAPI extends HttpServlet {
    ServletHelper helper = new ServletHelper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}
