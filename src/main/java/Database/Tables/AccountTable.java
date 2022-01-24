package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class AccountTable {
//    public void newAccount(String username, String password, String name) throws SQLException, ClassNotFoundException {
//        Account account = new Account(username,password,name);
//        addNewAccount(account);
//    }

    public void createTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE accounts "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null unique,"
                + "password VARCHAR (20) not null,"
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();
    }

    public int addNewAccount(Account account) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");

        String insertQuery = "INSERT INTO "
                + " accounts (name,username,password)"
                + " VALUES ("
                + "'" + account.getName() + "',"
                + "'" + account.getUsername() + "',"
                + "'" + account.getPassword() + "'"
                + ")";
        stmt.executeUpdate(insertQuery);
        System.out.println("# The account was successfully added in the database.");

        String getAccountID = "SELECT account_id FROM accounts WHERE username = '"+account.getUsername()+"'";
        ResultSet rs = stmt.executeQuery(getAccountID);
        rs.next();
        int account_id = rs.getInt("account_id");


        stmt.close();
        return account_id;
    }
}
