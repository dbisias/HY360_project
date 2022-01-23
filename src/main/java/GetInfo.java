import ServletHelper.ServletHelper;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "GetInfo", value = "/GetInfo")
public class GetInfo extends HttpServlet {

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

        EditSimpleUserTable usrtable = new EditSimpleUserTable();
        User loggedin = null;
        try {
            loggedin = usrtable.databaseToSimpleUser(logedin_id, password);
            if(loggedin!=null) {
                jsonreply = new JSONObject(usrtable.simpleUserToJSON((SimpleUser) loggedin));
                if(loggedin.getUsername().equals("admin")){
                    jsonreply.put("usertype","admin");
                }else{
                    jsonreply.put("usertype","user");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        EditDoctorTable doctable = new EditDoctorTable();
        try {
            loggedin = doctable.databaseToDoctor(logedin_id, password);
            if(loggedin!=null) {
                jsonreply = new JSONObject(doctable.doctorToJSON((Doctor) loggedin));
                jsonreply.put("usertype", "doctor");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(jsonreply==null){returnfailedlogin(response);}
        createResponse(response, 200, jsonreply.toString());
    }
}
