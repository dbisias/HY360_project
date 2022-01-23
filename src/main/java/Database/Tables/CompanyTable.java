package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Company;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CompanyTable {
    public void addCompanyfromJSON(String json) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Company company = gson.fromJson(json, Company.class);
        addNewComany(company);
    }

    public void addNewComany(Company company) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String insertQuery = "INSERT INTO "
                + " companies (account_id,name,username,password)"
                + " VALUES ("
                + "'" + company.getAccount_id() + "',"
                + "'" + company.getName() + "',"
                + "'" + company.getUsername() + "',"
                + "'" + company.getPassword() + "'"
                + ")";
        stmt.executeUpdate(insertQuery);
        System.out.println("# The company was successfully added in the database.");

        stmt.close();
    }

    public void createCompaniesTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE companies "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null,"
                + "password VARCHAR (20) not null,"
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }
}
