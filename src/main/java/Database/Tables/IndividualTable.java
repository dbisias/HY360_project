package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Company;
import Database.mainClasses.Individual;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IndividualTable {
    public void addIndividualfromJSON(String json) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Individual individual = gson.fromJson(json, Individual.class);
        addNewIndividual(individual);
    }

    public void addNewIndividual(Individual individual) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String insertQuery = "INSERT INTO "
                + " individuals (account_id,name,username,password)"
                + " VALUES ("
                + "'" + individual.getAccount_id() + "',"
                + "'" + individual.getName() + "',"
                + "'" + individual.getUsername() + "',"
                + "'" + individual.getPassword() + "'"
                + ")";
        System.out.println(insertQuery);
        stmt.executeUpdate(insertQuery);
        System.out.println("# The individual was successfully added in the database.");

        stmt.close();
    }

    public void createIndividualTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE individuals "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null,"
                + "password VARCHAR (20) not null,"
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public Account findAccount(String username, String password){

        ResultSet rs;
        Account user;
        Gson gson;
    
        Connection conn = DB_Connection.getConnection();
        Statement stmt  = con.createStatement();
        String query    = "SELECT username, password FROM individuals WHERE username = '" + 
        username + "' AND password = '" + password +"'";


        rs = stmt.executeQuery(query);
        DB_Connection.getResultToJSON(rs);
        gson = new Gson();
        user = gson.fromJson(json, Account.class);
        stmt.close();
        con.close();

        return user;
    }
}
