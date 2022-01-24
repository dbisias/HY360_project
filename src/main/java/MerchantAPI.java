import Database.Tables.MerchantTable;
import ServletHelper.ServletHelper;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "MerchantAPI", value = "/MerchantAPI")
public class MerchantAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonOut = new JSONObject();
        ServletHelper helper = new ServletHelper();
        MerchantTable mTable = new MerchantTable();
        jsonOut = mTable.getAll();
        helper.createResponse(response, 200, jsonOut.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
