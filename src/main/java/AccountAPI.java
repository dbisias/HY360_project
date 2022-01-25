import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.MerchantTable;
import Database.mainClasses.Company;
import Database.mainClasses.Individual;
import Database.mainClasses.Merchant;
import ServletHelper.ServletHelper;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet(name = "AccountAPI", value = "/AccountAPI")
public class AccountAPI extends HttpServlet {
    ServletHelper helper = new ServletHelper();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        IndividualTable iTable = new IndividualTable();
        ArrayList<Individual> iResults;
        JSONObject jsonOut = new JSONObject();
        Gson gson = new Gson();

        try {
            if (type.equals("good")) {
                iResults = iTable.getGoodUsers();

                for(Individual individual : iResults) {
                    jsonOut.append("individual", gson.toJson(individual, Individual.class));
                }
                helper.createResponse(response, 200, jsonOut.toString());
            }
            else if(type.equals("bad")) {
                iResults = iTable.getBadUsers();

                for(Individual individual : iResults) {
                    jsonOut.append("individual", gson.toJson(individual, Individual.class));
                }
                helper.createResponse(response, 200, jsonOut.toString());
            }
            else if(type.equals("merchant")) {
                MerchantTable mTable = new MerchantTable();
                Merchant merchant;
                merchant = mTable.getBest();

                helper.createResponse(response, 200, gson.toJson(merchant, Merchant.class));
            }
        }
        catch(Exception ex) {
            helper.createResponse(response, 403, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        try{
            if(jsonin.get("usertype").equals("individual")) {
                IndividualTable iTable = new IndividualTable();
                iTable.addAccountFromJSON(jsonin.toString());
            }
            else if(jsonin.get("usertype").equals("company")) {
                CompanyTable cTable = new CompanyTable();
                cTable.addAccountFromJSON(jsonin.toString());
            }
            else {
                MerchantTable mTable = new MerchantTable();
                mTable.addAccountFromJSON(jsonin.toString());
            }
        }catch(Exception ex) {
            System.out.println(ex.toString());
        }
        JSONObject jsonOut = new JSONObject();
//        jsonOut.put("reply", "Registration was successful");
        helper.createResponse(response, 200, "");

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        String username = (String) jsonIn.get("username");
        String password = (String) jsonIn.get("password");
        String usertype = (String) jsonIn.get("usertype");
        int acc_id = (Integer) jsonIn.get("acc_id");

        try{
            if(usertype.equals("individual")) {
                IndividualTable iTable = new IndividualTable();
                Individual individual = iTable.findAccount(acc_id);
                if(individual.getAmount_due() != 0) {
                    helper.createResponse(response, 403, "You can't delete your account. You still owe the CCC money");
                    return;
                }
                iTable.delAccount(acc_id);
                helper.createResponse(response, 200, "Account deleted successfully");
                return;

            }
            else if(usertype.equals("merchant")) {
                MerchantTable mTable = new MerchantTable();
                Merchant merchant = mTable.findAccount(acc_id);
                if(merchant.getAmount_due() != 0) {
                    helper.createResponse(response, 403, "You can't delete your account. You still owe the CCC money");
                    return;
                }
                mTable.delAccount(acc_id);
                helper.createResponse(response, 200, "Account deleted successfully");
                return;
            }
            else {
                CompanyTable cTable = new CompanyTable();
                Company company = cTable.findAccount(acc_id);
                if(company.getAmount_due() != 0) {
                    helper.createResponse(response, 403, "You can't delete your account. You still owe the CCC money");
                    return;
                }
                cTable.delAccount(acc_id);
                helper.createResponse(response, 200, "Account deleted successfully");
                return;
            }
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
