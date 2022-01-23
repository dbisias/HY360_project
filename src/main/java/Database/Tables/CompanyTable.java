package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Company;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class CompanyTable {
    Gson gson = new Gson();
    public String CompanyToJSON(Company company){
        return gson.toJson(company);
    }

    public void addCompanyfromJSON(String json) throws SQLException, ClassNotFoundException {
        Company company = gson.fromJson(json, Company.class);
        company.initfields();
        addNewComany(company);
    }

    public void addNewComany(Company company) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        SimpleDateFormat df = new SimpleDateFormat("YY-MM-DD");

        String insertQuery = "INSERT INTO "
                + " companies (account_id,name,username,password,billing_limit,expiration_date,ammount_due,remaining_ammount)"
                + " VALUES ("
                + "'" + company.getAccount_id() + "',"
                + "'" + company.getName() + "',"
                + "'" + company.getUsername() + "',"
                + "'" + company.getPassword() + "',"
                + "'" + company.getBillimit() + "',"
                + "'" + df.format(company.getExpiration_date()) + "',"
                + "'" + company.getAmmount_due() + "',"
                + "'" + company.getRemaining_amount()+ "'"
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
                + "billing_limit DOUBLE, "
                + "expiration_date DATE , "
                + "ammount_due DOUBLE, "
                + "remaining_ammount DOUBLE, "
                + "PRIMARY KEY ( account_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }
}
