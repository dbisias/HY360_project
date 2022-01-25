package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import java.util.Date;

public class TransactionsTable {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;


    public void createTable() throws SQLException, ClassNotFoundException {

        String query   = "CREATE TABLE transactions ("
        + "tid INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, "
        + "cli_acc_id INTEGER NOT NULL, "
        + "mer_acc_id INTEGER NOT NULL, "
        + "date DATE NOT NULL, "
        + "amount DOUBLE NOT NULL, "
        + "type VARCHAR (15) NOT NULL,"
        + "FOREIGN KEY (cli_acc_id) REFERENCES accounts(account_id), "
        + "FOREIGN KEY (mer_acc_id) REFERENCES accounts(account_id)) ";
        
        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(query);
        stmt.close();
        con.close();
    }

    public void insertTransaction(int cli_id, int mer_id, double amount, String type) throws SQLException, ClassNotFoundException {

        boolean flag;
        String query   = "SELECT EXISTS (SELECT 1 FROM individuals_view WHERE "
        + "account_id = '" + cli_id + "' LIMIT 1)"; // SELECT list is ignored due to 'EXISTS' / ultra fast approach
        
        
        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        flag = false;

        // TODO: change rs.next with stmt.execute(...);
        if ( !rs.next() ) {  // no results from the 'individuals' table

            rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM companies_view WHERE "
                    + "account_id = '" + cli_id + "' LIMIT 1)");

            if ( !rs.next() )  // empty set again
                flag = true;

            if ( !flag ) {

                rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM merchants_view WHERE "
                    + "account_id = '" + mer_id + "' LIMIT 1)");

                if ( !rs.next() )  // and again
                    flag = true;
            }
        }

        if ( flag ) {

            stmt.close();
            con.close();
    
            throw new SQLException("client with id = '" + cli_id + "' not found!");
            //return
        }

        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");

        stmt.executeUpdate("INSERT INTO transactions (cli_acc_id, mer_acc_id, date, amount, type) VALUES "
                + "('" + cli_id + "','" + mer_id + "','" + df.format(new Date()) + "','"  + amount + "','"+ type +"')");

        stmt.close();
        con.close();
    }

    public void refund(int tid) throws SQLException, ClassNotFoundException {

        double amount;
        String tmp;

        int cli_id;
        int mer_id;


        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT (cli_acc_id, merc_acc_id, amount) FROM "
            + "transactions WHERE tid = " + tid);

        rs.next();  // transaction exists for sure

        cli_id = rs.getInt(0);
        mer_id = rs.getInt(1);
        amount = rs.getDouble(2);

        rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM individuals_view WHERE "
        + "account_id = '" + cli_id + "' LIMIT 1)");  // SELECT list is ignored due to 'EXISTS' / ultra fast approach

        if ( !rs.next() )
            tmp = "companies";    // exists in 'companies' table
        else
            tmp = "individuals";  // exists in 'individuals' table

        stmt.executeUpdate("UPDATE " + tmp + " SET remaining_amount = "
            + "remaining_amount + " + amount + " WHERE "
            + "account_id = " + cli_id);

        stmt.executeUpdate("UPDATE merchants SET profit = "
            + "profit - ((100 - commission)/100 * " + amount + ") "
            + "WHERE account_id = " + mer_id);

        stmt.executeUpdate("UPDATE transactions SET type = "
        + "refunded WHERE tid = " + tid);
    }

    public void getTrans(int cli_id) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT (");
    }
}