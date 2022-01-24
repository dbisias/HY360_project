package ServletHelper;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletHelper {
    public void returnfailedlogin(HttpServletResponse response) {
        JSONObject jsonreply = new JSONObject();
        jsonreply.put("logged_in", false);
        jsonreply.put("message", "Username or Password is incorrect!");
        createResponse(response,403, jsonreply.toString());
    }
    public void createResponse(HttpServletResponse response, int statuscode, String data) {
        response.setStatus(statuscode);

        PrintWriter respwr = null;
        try {
            respwr = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        respwr.write(data);
        response.setContentType("application/text");
        response.setCharacterEncoding("UTF-8");
    }
}
