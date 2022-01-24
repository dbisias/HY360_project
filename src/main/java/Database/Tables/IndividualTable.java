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

public class IndividualTable implements DBTable {
    
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
        String insertQuery = "INSERT INTO "
                + " individuals (account_id,billing_limit,expiration_date,amount_due,remaining_amount)"
                + " VALUES ("
                + "'" + account_id + "',"
                + "'" + individual.getBillimit() + "',"
                + "'" + df.format(individual.getExpiration_date()) + "',"
                + "'" + individual.getAmount_due() + "',"
                + "'" + individual.getRemaining_amount()+ "'"
                + ")";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        System.out.println(insertQuery);
        stmt.executeUpdate(insertQuery);
        System.out.println("# The individual was successfully added in the database.");

        con.close();
        stmt.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE individuals "
                + "(account_id INTEGER not NULL AUTO_INCREMENT unique, "
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
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

    public Individual findAccount(String username, String password) throws SQLException, ClassNotFoundException {

        Individual user;
        String json;

        String query   = "SELECT * FROM individuals_view WHERE username = '" +
        username + "' AND password = '" + password +"'";


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

    @Override
    public int buy(int cli_id, double amount) throws ClassNotFoundException, SQLException {

        double tmp;
        int remain;
        int ret;


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT remaining_ammount FROM individuals_view WHERE "
            + "account_id = '" + cli_id + "'");

        remain = rs.getInt("remaining_ammount");

        if ( remain >= amount ) {

            stmt.executeUpdate("UPDATE individuals SET remaining_ammount = '"
                + (remain - amount) + "' WHERE account_id = '" + cli_id + "'");

            ret = 0;
        }
        else {

            tmp = amount - remain;
            rs = stmt.executeQuery("SELECT billing_limit, amount_due FROM individuals_view WHERE "
                + "account_id = '" + cli_id + "'");

            rs.next();
            tmp = this.rs.getDouble("amount_due") + (amount - remain);
            if ( this.rs.getDouble("billing_limit") >= tmp )
                ret = -1;  // error - exceding billing-limit
            else {

                stmt.executeUpdate("UPDATE individuals SET remaining_ammount = '"
                    + 0.0 + "', amount_due = '" + tmp
                    + "' WHERE account_id = '" + cli_id + "'");

                ret = 1;
            }
        }

        stmt.close();
        con.close();

        return ret;
    }
}
