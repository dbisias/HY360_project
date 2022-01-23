package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Company;
import Database.mainClasses.Individual;
import Database.mainClasses.Merchant;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MerchantTable {
    Gson gson = new Gson();
    public String MerchantToJSON(Merchant merchant){
        return gson.toJson(merchant);
    }

    public void addMerchantfromJSON(String json) throws SQLException, ClassNotFoundException {
        Merchant merchant = gson.fromJson(json, Merchant.class);
        merchant.initfields(0);
        addNewMerchant(merchant);
    }

    public void addNewMerchant(Merchant merchant) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String insertQuery = "INSERT INTO "
                + " merchants (account_id,name,username,password,comission,profit,ammount_due)"
                + " VALUES ("
                + "'" + merchant.getAccount_id() + "',"
                + "'" + merchant.getName() + "',"
                + "'" + merchant.getUsername() + "',"
                + "'" + merchant.getPassword() + "',"
                + "'" + merchant.getComission() + "',"
                + "'" + merchant.getProfit() + "',"
                + "'" + merchant.getAmmount_due() + "'"
                + ")";
        stmt.executeUpdate(insertQuery);
        System.out.println("# The merchant was successfully added in the database.");

        stmt.close();
    }

    public void createMerchantTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE merchants "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null unique, "
                + "password VARCHAR (20) not null,"
                + "comission DOUBLE, "
                + "profit DOUBLE, "
                + "ammount_due DOUBLE, "
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public Account findAccount(String username, String password) throws SQLException, ClassNotFoundException {

        ResultSet rs;
        Account user;
        String json;

        Connection con = DB_Connection.getConnection();
        Statement stmt  = con.createStatement();
        String query    = "SELECT username, password FROM merchants WHERE username = '" + 
        username + "' AND password = '" + password +"'";

        rs = stmt.executeQuery(query);
        rs.next();
        json = DB_Connection.getResultsToJSON(rs);
        gson = new Gson();
        user = gson.fromJson(json, Account.class);
        stmt.close();
        con.close();

        return user;
    }
}
