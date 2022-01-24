package main.java.Database.Tables;

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

    Gson gson = new Gson();

    public void createTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query   = "CREATE TABLE transactions ("
                + "tid INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "cli_acc_id INTEGER NOT NULL, "
                + "mer_acc_id INTEGER NOT NULL, "
                + "date DATE NOT NULL, "
                + "ammount DOUBLE NOT NULL);";

        stmt.execute(query);
        stmt.close();
        con.close();
    }

    /**
     * @param cli_id The client ID
     * @param mer_id The mechant ID
     * @param amount The amount to be paid
     */
    public void insertTransaction(int cli_id, int mer_id, double amount) throws SQLException, ClassNotFoundException {

        ResultSet rs;
        String json;
        boolean flag;

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query   = "SELECT EXISTS (SELECT 1 FROM individuals WHERE "
                + "account_id = '" + cli_id + "' LIMIT 1)"; // SELECT list is ignored due to 'EXISTS' / ultra fast approach


        rs = stmt.executeQuery(query);
        flag = false;

        // TODO: change rs.next with stmt.execute(...);
        if ( !rs.next() ) {  // no results from the 'individuals' table

            rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM companies WHERE "
                    + "account_id = '" + cli_id + "' LIMIT 1)");

            if ( !rs.next() )  // empty set again
                flag = true;

            if ( !flag ) {

                rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM merchants WHERE "
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

        stmt.executeUpdate("INSERT INTO transactions (cli_acc_id, mer_acc_id, date, amount) VALUES "
                + "('" + cli_id + "','" + mer_id + "','" + df.format(new Date()) + "','"  + amount + "')");

        stmt.close();
        con.close();
    }
}