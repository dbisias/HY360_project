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
        String logedin_id = session.getAttribute("logged_in").toString();
        String password = (String) session.getAttribute("password");
        JSONObject jsonreply = null;

        Account loggedin = null;
        try {
            if(it.findAccount(logedin_id, password)!=null) {
                jsonreply = new JSONObject(it.IndividualToJSON(it.findAccount(logedin_id, password)));
                jsonreply.put("usertype","individual");
            }

            if(ct.findAccount(logedin_id, password)!=null) {
                jsonreply = new JSONObject(ct.CompanyToJSON(ct.findAccount(logedin_id, password)));
                jsonreply.put("usertype", "company");
            }

            if(mt.findAccount(logedin_id, password)!=null) {
                jsonreply = new JSONObject(mt.MerchantToJSON(mt.findAccount(logedin_id, password)));
                jsonreply.put("usertype", "merchant");
            }

        } catch (Exception e) {
            e.printStackTrace();
            helper.returnfailedlogin(response);
        }

        if(jsonreply==null){helper.returnfailedlogin(response);}
        helper.createResponse(response, 200, jsonreply.toString());
    }
}
