package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Individual;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class IndividualTable implements DBTable {
    Gson gson = new Gson();

    public String accountToJSON(Individual individual) {

        return gson.toJson(individual);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {

        Individual individual = gson.fromJson(json, Individual.class);
        individual.initfields();
        addNewAccount(individual);
    }

    public void addNewAccount(Individual individual) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");

        String insertQuery = "INSERT INTO "
                + " individuals (account_id,name,username,password,billing_limit,expiration_date,ammount_due,remaining_ammount)"
                + " VALUES ("
                + "'" + individual.getAccount_id() + "',"
                + "'" + individual.getName() + "',"
                + "'" + individual.getUsername() + "',"
                + "'" + individual.getPassword() + "',"
                + "'" + individual.getBillimit() + "',"
                + "'" + df.format(individual.getExpiration_date()) + "',"
                + "'" + individual.getAmmount_due() + "',"
                + "'" + individual.getRemaining_amount()+ "'"
                + ")";
        System.out.println(insertQuery);
        stmt.executeUpdate(insertQuery);
        System.out.println("# The individual was successfully added in the database.");

        stmt.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE individuals "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null unique,"
                + "password VARCHAR (20) not null,"
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
                + "ammount_due DOUBLE, "
                + "remaining_ammount DOUBLE, "
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }


    public Individual findAccount(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet rs;
        Individual user;

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String query   = "SELECT username, password FROM individuals WHERE username = '" + 
        username + "' AND password = '" + password +"'";


        rs = stmt.executeQuery(query);
        rs.next();
        json = DB_Connection.getResultsToJSON(rs);
        user = gson.fromJson(json, Account.class);
        stmt.close();
        con.close();

        return user;
    }
}
