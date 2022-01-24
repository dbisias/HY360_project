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

    private Gson gson = new Gson();
    private ResultSet rs;
    private Connection con;
    private Statement stmt;

    public String accountToJSON(Company company) {

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

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate(insertQuery);
        System.out.println("# The company was successfully added in the database.");

        stmt.close();
        con.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE companies "
                + "(account_id INTEGER not NULL AUTO_INCREMENT unique, "
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
                + "amount_due DOUBLE, "
                + "remaining_amount DOUBLE, "
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    @Override
    public int buy(int cli_id, double amount) throws SQLException, ClassNotFoundException {

        double tmp;
        double remain;
        int ret;


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT remaining_amount FROM companies_view WHERE "
            + "account_id = '" + cli_id + "'");

        rs.next();
        remain = rs.getDouble("remaining_amount");

        if ( remain >= amount ) {

            stmt.executeUpdate("UPDATE companies SET remaining_amount = '"
                + (remain - amount) + "' WHERE account_id = '" + cli_id + "'");

            ret = 0;
        }
        else {

            tmp = amount - remain;
            rs = stmt.executeQuery("SELECT billing_limit, amount_due FROM companies_view WHERE "
                + "account_id = '" + cli_id + "'");

            rs.next();
            tmp = this.rs.getDouble("amount_due") + (amount - remain);
            if ( this.rs.getDouble("billing_limit") < tmp )
                ret = -1;  // error - exceding billing-limit
            else {

                stmt.executeUpdate("UPDATE companies SET remaining_amount = '"
                    + 0.0 + "', amount_due = '" + tmp
                    + "' WHERE account_id = '" + cli_id + "'");

                ret = 1;
            }
        }

        stmt.close();
        con.close();

        return ret;
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
        String query   = "SELECT * FROM companies_view WHERE username = '" +
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
