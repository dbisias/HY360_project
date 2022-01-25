package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Individual;
import Exceptions.InsufficientBalanceException;
import Exceptions.UserNotFoundException;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IndividualTable {
    
    private Gson gson = new Gson();
    private ResultSet rs;
    private Connection con;
    private Statement stmt;


    public IndividualTable() {

        super();
    }

    public String accountToJSON(Individual individual) {

        return gson.toJson(individual);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {

        Individual individual = gson.fromJson(json, Individual.class);
        individual.initfields();
        addNewAccount(individual);
    }

    public void addNewAccount(Individual individual) throws SQLException, ClassNotFoundException {

        AccountTable at = new AccountTable();
        int account_id = at.addNewAccount((Account) individual);

        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");
        String insertQuery;
        if(individual.getCompany_account_id() != 0) {
            insertQuery = "INSERT INTO "
                    + " individuals (account_id,billing_limit,expiration_date,amount_due,remaining_amount,company_account_id)"
                    + " VALUES ("
                    + "'" + account_id + "',"
                    + "'" + individual.getBillimit() + "',"
                    + "'" + df.format(individual.getExpiration_date()) + "',"
                    + "'" + individual.getAmount_due() + "',"
                    + "'" + individual.getRemaining_amount()+ "',"
                    + "" + individual.getCompany_account_id()+ ""
                    + ")";
        }
        else {
            insertQuery = "INSERT INTO "
                    + " individuals (account_id,billing_limit,expiration_date,amount_due,remaining_amount)"
                    + " VALUES ("
                    + "'" + account_id + "',"
                    + "'" + individual.getBillimit() + "',"
                    + "'" + df.format(individual.getExpiration_date()) + "',"
                    + "'" + individual.getAmount_due() + "',"
                    + "'" + individual.getRemaining_amount()+ "'"
                    + ")";
        }


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        System.out.println(insertQuery);
        stmt.executeUpdate(insertQuery);
        System.out.println("# The individual was successfully added in the database.");

        con.close();
        stmt.close();
    }

    public Individual findAccount(int cli_id) throws SQLException, ClassNotFoundException {

        Individual user;
        String json;

        String query   = "SELECT * FROM individuals_view WHERE account_id = '" + cli_id + "'";


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        if ( !rs.next() )
            return null;

        json = DB_Connection.getResultsToJSON(rs);
        user = gson.fromJson(json, Individual.class);
        stmt.close();
        con.close();

        return user;
    }

    public Individual login(String username, String password) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM individuals_view WHERE "
            + "username = '" + username + "'AND password = '" + password + "'");

        if( !rs.next() )
            return null;

        return gson.fromJson(DB_Connection.getResultsToJSON(rs), Individual.class);
    }

    public ArrayList<Individual> getGoodUsers() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM individuals_view WHERE "
        + "amount_due = '0'");

        if ( !rs.next() )
            return null;

        ArrayList<Individual> ret = new ArrayList<Individual>();
        ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Individual.class));

        while ( rs.next() )
            ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Individual.class));

        stmt.close();
        con.close();

        return ret;
    }

    public ArrayList<Individual> getBadUsers() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM individuals_view WHERE "
        + "amount_due > '0' ORDER BY amount_due DESC");

        if ( !rs.next() )
            return null;

        ArrayList<Individual> ret = new ArrayList<Individual>();
        ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Individual.class));

        while ( rs.next() )
            ret.add(gson.fromJson(DB_Connection.getResultsToJSON(rs), Individual.class));

        stmt.close();
        con.close();

        return ret;
    }

    public void payDebt(int cli_id, double amount) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate("UPDATE individuals SET amount_due = '"
            + "amount_due - " + amount + " WHERE account_id = '"
            + cli_id + "'");

        stmt.executeUpdate("UPDATE individuals SET remaining_ammount = "
            + "remaining_amount - " + amount + " WHERE account_id = '"
            + cli_id + "'");

        con.close();
        stmt.close();
    }

    public void delAccount(int acc_id) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate("DELETE FROM individuals WHERE account_id = '"
            + acc_id + "'");

        stmt.executeUpdate("DELETE FROM accounts WHERE accound_id = '"
            + acc_id + "'");

        stmt.close();
        con.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE individuals "
                + "(account_id INTEGER not NULL AUTO_INCREMENT unique, "
                + "billing_limit DOUBLE, "
                + "expiration_date DATE, "
                + "amount_due DOUBLE, "
                + "remaining_amount DOUBLE, "
                + "company_account_id INTEGER, "
                + "FOREIGN KEY (company_account_id) REFERENCES accounts(account_id), "
                + "FOREIGN KEY (account_id) REFERENCES accounts(account_id))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        System.out.println(sql);
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public int buy(int cli_id, int mer_id, double amount) throws ClassNotFoundException, SQLException {

        double tmp;
        double remain;
        int ret;


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT remaining_amount FROM individuals_view WHERE "
            + "account_id = '" + cli_id + "'");

        rs.next();
        remain = rs.getDouble("remaining_amount");

        if ( remain >= amount ) {

            stmt.executeUpdate("UPDATE individuals SET remaining_amount = '"
                + (remain - amount) + "' WHERE account_id = '" + cli_id + "'");

            ret = 0;
        }
        else {

            rs = stmt.executeQuery("SELECT billing_limit, amount_due FROM individuals_view WHERE "
                + "account_id = '" + cli_id + "'");

            rs.next();
            tmp = this.rs.getDouble("amount_due") + (amount - remain);
            if ( this.rs.getDouble("billing_limit") < tmp )
                ret = -1;  // error - exceeding billing-limit
            else {

                stmt.executeUpdate("UPDATE individuals SET remaining_amount = '"
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
