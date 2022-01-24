package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Company;
import Exceptions.UserNotFoundException;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;


public class CompanyTable implements DBTable {
    Gson gson = new Gson();
    public String accountToJSON(Company company){
        return gson.toJson(company);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {
        Company company = gson.fromJson(json, Company.class);
        company.initfields();
        addNewAccount(company);
    }

    public void addNewAccount(Company company) throws SQLException, ClassNotFoundException {
        AccountTable at = new AccountTable();
        int account_id = at.addNewAccount((Account) company);

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");

        String insertQuery = "INSERT INTO "
                + " companies (account_id,billing_limit,expiration_date,amount_due,remaining_amount)"
                + " VALUES ("
                + "'" + account_id + "',"
                + "'" + company.getBillimit() + "',"
                + "'" + df.format(company.getExpiration_date()) + "',"
                + "'" + company.getAmount_due() + "',"
                + "'" + company.getRemaining_amount()+ "'"
                + ")";
        stmt.executeUpdate(insertQuery);
        System.out.println("# The company was successfully added in the database.");

        stmt.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE companies "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
                + "amount_due DOUBLE, "
                + "remaining_amount DOUBLE, "
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    @Override
    public void buy(int cli_id, int amount) throws ClassNotFoundException {

    }

    /**
     * Retuns either a non-null account if credentials are correct
     * or an exception if they don't match an existing user.
     * @param username
     * @param password
     * @return user account
     */
    public Company findAccount(String username, String password) throws SQLException, ClassNotFoundException {

        ResultSet rs;
        Company user;

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query   = "SELECT * FROM companies WHERE username = '" +
        username + "' AND password = '" + password +"'";

        rs = stmt.executeQuery(query);

        if( !rs.next() )
            return null;

        String json = DB_Connection.getResultsToJSON(rs);
        user = gson.fromJson(json, Company.class);
        stmt.close();
        con.close();

        return user;
    }
}
