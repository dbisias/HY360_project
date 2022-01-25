import Database.Tables.*;
import Database.mainClasses.Account;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import ServletHelper.ServletHelper;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    ServletHelper helper = new ServletHelper();
    CompanyTable ct = new CompanyTable();
    IndividualTable it = new IndividualTable();
    MerchantTable mt = new MerchantTable();

    private void login(HttpServletRequest request, HttpServletResponse response, Account logged_in, String usertype){
        if(usertype.equals("")){return;}
        JSONObject jsonreply = new JSONObject();
        jsonreply.put("logged_in", true);
        jsonreply.put("usertype", usertype);

        HttpSession session = null;
        session = request.getSession(true);
        try {
            session.setAttribute("logged_in", logged_in.getAccount_id());
        }catch (NullPointerException e){
            helper.createResponse(response,403,e.getMessage());
            return;
        }

        helper.createResponse(response,200,jsonreply.toString());
        return;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usertype; //0 for unknown,1 for user,2 for doctor,3 for admin
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonin = new JSONObject(tokener);

        Account logged_in = null;
        try {
            logged_in = it.login((String) jsonin.get("username"),(String) jsonin.get("password"));
            if(logged_in!=null) {
                usertype = "individual";
                login(request,response,logged_in,usertype);
                return;
            }

            logged_in = ct.login((String) jsonin.get("username"),(String) jsonin.get("password"));
            if(logged_in!=null){
                usertype="company";
                login(request,response,logged_in,usertype);
                return;
            }

            logged_in = mt.login((String) jsonin.get("username"),(String) jsonin.get("password"));
            if(logged_in!=null){
                usertype="merchant";
                login(request,response,logged_in,usertype);
                return;
            }
        } catch (Exception  e) {
            e.printStackTrace();
            helper.createResponse(response,403,e.getMessage());
            return;
        }
        helper.createResponse(response,200,"");
    }
}
