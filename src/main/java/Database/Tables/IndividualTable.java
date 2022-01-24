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


    public IndividualTable(){

        this.gson = new Gson();
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

    @Override
    public void buy(int cli_id, int amount) throws UserNotFoundException, ClassNotFoundException {

        Account user;
        int temp;

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT remaining_ammount FROM individuals WHERE "
            + "account_id = '" + cli_id + "'");

        if ( !rs.next() ) {

            stmt.close();
            con.close();

            throw new UserNotFoundException();
        }

        temp = rs.getInt("remaining_ammount");

        if ( temp > amount ) {

            stmt.close();
            con.close();

            throw new InsufficientBalanceException();
        }

        stmt.executeUpdate("UPDATE individuals SET remaining_ammount = '"
            + (amount - temp) + "' WHERE account_id = '" + cli_id + "'");

        stmt.close();
        con.close();
    }
}
