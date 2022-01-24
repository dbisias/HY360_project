import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.MerchantTable;
import ServletHelper.ServletHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Register", value = "/Register")
public class Register extends HttpServlet {
    ServletHelper helper = new ServletHelper();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}
