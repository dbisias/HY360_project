import ServletHelper.ServletHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.naming.spi.NamingManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import main.java.Database.Tables.TransactionsTable;

@WebServlet(name = "TransactionsAPI", value = "/TransactionsAPI")
public class TransactionsAPI extends HttpServlet {
    ServletHelper helper = new ServletHelper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader inputJSONfromClient = request.getReader();
        JSONTokener tokener = new JSONTokener(inputJSONfromClient);
        JSONObject jsonIn = new JSONObject(tokener);

        int merchant_id = Integer.parseInt((String) jsonIn.get("merchant_id"));
        int account_id = (int) jsonIn.get("account_id");
        double amount = Double.parseDouble((String) jsonIn.get("amount"));
        int as_company = (int) jsonIn.get("as_company");

        TransactionsTable tTable = new TransactionsTable();

        try {
            if(as_company == 0)
                tTable.insertTransaction(account_id, merchant_id, amount);
            else
                tTable.insertTransaction(as_company, merchant_id, amount);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            helper.createResponse(response, 403, e.getMessage());
            return;
        }
        helper.createResponse(response, 200, "Transaction added successfully");
    }
}
