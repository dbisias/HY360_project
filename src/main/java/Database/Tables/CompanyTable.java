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
import java.util.ArrayList;


public class CompanyTable {

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

    public Company findAccount(int cli_id) throws SQLException, ClassNotFoundException {

        Company user;

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM companies_view WHERE "
            + "account_id = '" + cli_id + "'");

        if( !rs.next() )
            return null;

        String json = DB_Connection.getResultsToJSON(rs);
        user = gson.fromJson(json, Company.class);
        stmt.close();
        con.close();

        return user;
    }

    public ArrayList<Company> getGoodUsers() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Companys_view WHERE "
        + "amount_due = '0'");

        if ( !rs.next() )
            return null;

        ArrayList<Company> ret = new ArrayList<Company>();

        while ( rs.next() )
            ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Company.class));

        stmt.close();
        con.close();

        return ret;
    }

    public ArrayList<Company> getBadUsers() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM companies_view WHERE "
        + "amount_due > '0' ORDER BY amount_due DESC");

        if ( !rs.next() )
            return null;

        ArrayList<Company> ret = new ArrayList<Company>();

        while ( rs.next() )
            ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Company.class));

        stmt.close();
        con.close();

        return ret;
    }

    public void payDebt(int cli_id, double amount) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate("UPDATE companies SET amount_due = "
            + "amount_due - " + amount + " WHERE account_id = '"
            + cli_id + "'");

        stmt.executeUpdate("UPDATE companies SET remaining_amount = "
            + "remaining_amount - " + amount + " WHERE account_id = '"
            + cli_id + "'");

        stmt.close();
        con.close();
    }

    public Company login(String username, String password) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM companies_view WHERE "
            + "username = '" + username + "'AND password = '" + password + "'");

        if( !rs.next() ) {

            stmt.close();
            con.close();

            return null;
        }
        Company logged_in = gson.fromJson(DB_Connection.getResultsToJSON(rs), Company.class);

        stmt.close();
        con.close();

        return logged_in;
    }

    public void delAccount(int acc_id) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate("DELETE FROM companies WHERE account_id = '"
            + acc_id + "'");

        stmt.executeUpdate("DELETE FROM accounts WHERE accound_id = '"
            + acc_id + "'");

        stmt.close();
        con.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE companies "
                + "(account_id INTEGER not NULL AUTO_INCREMENT unique, "
                + "billing_limit DOUBLE, "
                + "expiration_date DATE, "
                + "amount_due DOUBLE, "
                + "remaining_amount DOUBLE, "
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public int buy(int cli_id, int mer_id, double amount) throws SQLException, ClassNotFoundException {

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

        if ( ret != -1 )
            stmt.executeUpdate("UPDATE merchants SET profit = "
                + "profit + " + " ((100 - commission) / 100) * " + amount
                + " WHERE account_id = '" + mer_id + "'");

        stmt.close();
        con.close();

        return ret;
    }

}
