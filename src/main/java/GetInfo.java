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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("logged_in")==null){
            helper.returnfailedlogin(response);
            return;
        }
        int loggedin_id = (int) session.getAttribute("logged_in");
        String company_id = (String) request.getParameter("company_id");
        JSONObject jsonreply = null;

        try {
            if(company_id != null) {
                int cid = Integer.parseInt(company_id);
                jsonreply = new JSONObject((ct.accountToJSON(ct.findAccount(cid))));
                jsonreply.put("usertype", "company");
                helper.createResponse(response, 200, jsonreply.toString());
            }else if(it.findAccount(loggedin_id)!=null) {
                jsonreply = new JSONObject(it.accountToJSON(it.findAccount(loggedin_id)));
                jsonreply.put("usertype","individual");
                helper.createResponse(response, 200, jsonreply.toString());
            }else if(ct.findAccount(loggedin_id)!=null) {
                jsonreply = new JSONObject(ct.accountToJSON(ct.findAccount(loggedin_id)));
                jsonreply.put("usertype", "company");
                helper.createResponse(response, 200, jsonreply.toString());
            }else if(mt.findAccount(loggedin_id)!=null) {
                jsonreply = new JSONObject(mt.accountToJSON(mt.findAccount(loggedin_id)));
                jsonreply.put("usertype", "merchant");
                helper.createResponse(response, 200, jsonreply.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            helper.returnfailedlogin(response);
        }
    }
}
