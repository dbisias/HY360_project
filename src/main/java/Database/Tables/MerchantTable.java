package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Merchant;
import Database.mainClasses.Merchant;
import Exceptions.UserNotFoundException;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MerchantTable {

    private Gson gson;
    private Connection con;
    private Statement stmt;
    private ResultSet rs;


    public MerchantTable() {

        gson = new Gson();
    }

    public String accountToJSON(Merchant merchant) {
        return gson.toJson(merchant);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {
        Merchant merchant = gson.fromJson(json, Merchant.class);
        merchant.initfields(10);
        addNewAccount(merchant);
    }

    public void addNewAccount(Merchant merchant) throws SQLException, ClassNotFoundException {
        AccountTable at = new AccountTable();
        int account_id = at.addNewAccount((Account) merchant);

        String insertQuery = "INSERT INTO "
                + " merchants (account_id,commission,profit,amount_due)"
                + " VALUES ("
                + "'" + account_id + "',"
                + "'" + merchant.getCommission() + "',"
                + "'" + merchant.getProfit() + "',"
                + "'" + merchant.getAmount_due() + "'"
                + ")";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate(insertQuery);
        System.out.println("# The merchant was successfully added in the database.");

        stmt.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE merchants "
                + "(account_id INTEGER not NULL AUTO_INCREMENT unique, "
                + "commission DOUBLE, "
                + "profit DOUBLE, "
                + "amount_due DOUBLE, "
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public Merchant findAccount(int cli_id) throws SQLException, ClassNotFoundException {
        
        Merchant user;

        String query   = "SELECT * FROM merchants_view WHERE "
            + "account_id = '" + cli_id + "'";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        if ( !rs.next() )
            return null;

        user = gson.fromJson(DB_Connection.getResultsToJSON(rs), Merchant.class);
        stmt.close();
        con.close();

        return user;
    }

    public Merchant login(String username, String password) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM merchants_view WHERE "
            + "username = '" + username + "'AND password = '" + password + "'");

        if( !rs.next() ) {

            stmt.close();
            con.close();

            return null;
        }

        Merchant logged_in = gson.fromJson(DB_Connection.getResultsToJSON(rs), Merchant.class);

        stmt.close();
        con.close();

        return logged_in;
    }

    public JSONObject getAll() throws SQLException, ClassNotFoundException {

        JSONObject ret;

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT account_id, name FROM merchants_view");

        if ( !rs.next() ) {

            stmt.close();
            con.close();

            return null;
        }

        ret = new JSONObject();
        do{
            ret.append("merchants",DB_Connection.getResultsToJSON(rs));
        }while(rs.next());

        stmt.close();
        con.close();

        return ret;
    }

    public Merchant getBest() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM merchants_view WHERE "
            + "profit = (SELECT MAX(profit) FROM merchants_view) LIMIT 1");
        
        if ( !rs.next() ) {

            stmt.close();
            con.close();

            return null;
        }

        stmt.executeUpdate("UPDATE merchants SET commission = "
            + " 0.95 * commission WHERE account_id = " + rs.getInt("account_id"));

        stmt.close();
        con.close();

        return gson.fromJson(DB_Connection.getResultsToJSON(rs), Merchant.class);
    }

    public void delAccount(int acc_id) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate("DELETE FROM merchants WHERE account_id = '"
            + acc_id + "'");

        stmt.executeUpdate("DELETE FROM accounts WHERE accound_id = '"
            + acc_id + "'");

        stmt.close();
        con.close();
    }

}
