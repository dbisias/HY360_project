import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.MerchantTable;
import Database.mainClasses.Account;
import Database.mainClasses.Company;
import Database.mainClasses.Individual;
import Database.mainClasses.Merchant;
import ServletHelper.ServletHelper;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GetInfo", value = "/GetInfo")
public class GetInfo extends HttpServlet {
    ServletHelper helper = new ServletHelper();
    IndividualTable it = new IndividualTable();
    CompanyTable ct = new CompanyTable();
    MerchantTable mt = new MerchantTable();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("logged_in")==null){
            helper.returnfailedlogin(response);
            return;
        }
        String loggedin_id = session.getAttribute("logged_in").toString();
        String password = (String) session.getAttribute("password");
        JSONObject jsonreply = null;

        try {
            if(it.findAccount(loggedin_id, password)!=null) {
                jsonreply = new JSONObject(it.accountToJSON(it.findAccount(loggedin_id, password)));
                jsonreply.put("usertype","individual");
                helper.createResponse(response, 200, jsonreply.toString());
                return;
            }

            if(ct.findAccount(loggedin_id, password)!=null) {
                jsonreply = new JSONObject(ct.accountToJSON(ct.findAccount(loggedin_id, password)));
                jsonreply.put("usertype", "company");
                helper.createResponse(response, 200, jsonreply.toString());
                return;
            }

            if(mt.findAccount(loggedin_id, password)!=null) {
                jsonreply = new JSONObject(mt.accountToJSON(mt.findAccount(loggedin_id, password)));
                jsonreply.put("usertype", "merchant");
                helper.createResponse(response, 200, jsonreply.toString());
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            helper.returnfailedlogin(response);
        }
    }
}
