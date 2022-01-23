package Database.Init;

import Database.Tables.CompanyTable;
import Database.Tables.IndividualTable;
import Database.Tables.MerchantTable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import static Database.Connection.DB_Connection.getInitialConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class InitDatabase {

    IndividualTable it = new IndividualTable();
    CompanyTable ct = new CompanyTable();
    MerchantTable mt = new MerchantTable();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE CCC_db");
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {
        it.createIndividualTable();
        ct.createCompaniesTable();
        mt.createMerchantTable();
    }

    public void addToDatabaseExamples() throws ClassNotFoundException {
        String individual = "{\"account_id\":\"1\",\"name\":\"Christos Papastamos\",\"username\":\"papastam\",\"password\":\"123456\"}";
        String company = "{\"account_id\":\"2\",\"name\":\"CCC\",\"username\":\"ccc\",\"password\":\"123456\"}";
        String merchant = "{\"account_id\":\"3\",\"name\":\"Dimitris Bisias\",\"username\":\"b1s\",\"password\":\"123456\"}";

        try {
            it.addIndividualfromJSON(individual);
            ct.addCompanyfromJSON(company);
            mt.addMerchantfromJSON(merchant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
