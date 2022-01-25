import Database.Tables.MerchantTable;
import ServletHelper.ServletHelper;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "MerchantAPI", value = "/MerchantAPI")
public class MerchantAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonOut;
        ServletHelper helper = new ServletHelper();
        MerchantTable mTable = new MerchantTable();
        try {
            jsonOut = mTable.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            helper.createResponse(response,403,e.getMessage());
            return;
        }
        helper.createResponse(response, 200, jsonOut.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
