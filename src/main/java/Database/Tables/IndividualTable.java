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
    ResultSet rs;
    Connection con;
    Statement stmt;

    public String accountToJSON(Individual individual) {

        return gson.toJson(individual);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {

        Individual individual = gson.fromJson(json, Individual.class);
        individual.initfields();
        addNewAccount(individual);
    }

    public void addNewAccount(Individual individual) throws SQLException, ClassNotFoundException {

        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");
        String insertQuery = "INSERT INTO "
                + " individuals (name,username,password,billing_limit,expiration_date,ammount_due,remaining_ammount)"
                + " VALUES ("
                + "'" + individual.getName() + "',"
                + "'" + individual.getUsername() + "',"
                + "'" + individual.getPassword() + "',"
                + "'" + individual.getBillimit() + "',"
                + "'" + df.format(individual.getExpiration_date()) + "',"
                + "'" + individual.getAmmount_due() + "',"
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
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null unique,"
                + "password VARCHAR (20) not null,"
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
                + "ammount_due DOUBLE, "
                + "remaining_ammount DOUBLE, "
                + "PRIMARY KEY ( account_id ))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public Individual findAccount(String username, String password) throws SQLException, ClassNotFoundException {

        Individual user;
        String json;

        String query   = "SELECT username, password FROM individuals WHERE username = '" + 
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

    public void buy(int cli_id, int amount) throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        // stmt.executeQuery("");

        stmt.close();
        con.close();
    }
}
