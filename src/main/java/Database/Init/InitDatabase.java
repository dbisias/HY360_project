package Database.Init;

import Database.Connection.DB_Connection;
import Database.Tables.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import static Database.Connection.DB_Connection.getInitialConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class InitDatabase {

    AccountTable at = new AccountTable();
    IndividualTable it = new IndividualTable();
    CompanyTable ct = new CompanyTable();
    MerchantTable mt = new MerchantTable();
    TransactionsTable tt = new TransactionsTable();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        InitDatabase init = new InitDatabase();
        init.initDatabase();
        init.initTables();
        init.addToDatabaseExamples();
        init.initViews();
    }

    private void initViews() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        stmt.execute("CREATE VIEW individuals_view AS SELECT accounts.account_id,accounts.name,accounts.username,accounts.password,individuals.billing_limit,individuals.expiration_date,individuals.amount_due,individuals.remaining_amount,individuals.company_account_id FROM accounts JOIN individuals ON accounts.account_id=individuals.account_id");
        stmt.execute("CREATE VIEW companies_view AS SELECT accounts.account_id,accounts.name,accounts.username,accounts.password,companies.billing_limit,companies.expiration_date,companies.amount_due,companies.remaining_amount FROM accounts JOIN companies ON accounts.account_id=companies.account_id");
        stmt.execute("CREATE VIEW merchants_view AS SELECT accounts.account_id,accounts.name,accounts.username,accounts.password,merchants.commission,merchants.profit,merchants.amount_due FROM accounts JOIN merchants ON accounts.account_id=merchants.account_id");
        stmt.close();
        con.close();
    }

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection conn = getInitialConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("DROP DATABASE CCC_db");
        stmt.execute("CREATE DATABASE CCC_db");
        stmt.close();
        conn.close();
    }

    public void initTables() throws SQLException, ClassNotFoundException {
        at.createTable();
        it.createTable();
        ct.createTable();
        mt.createTable();
        tt.createTable();
    }

    public void addToDatabaseExamples() throws ClassNotFoundException {
        String individual = "{\"account_id\":\"1\",\"name\":\"Christos Papastamos\",\"username\":\"papastam\",\"password\":\"123456\"}";
        String company = "{\"account_id\":\"2\",\"name\":\"CCC\",\"username\":\"ccc\",\"password\":\"123456\"}";
        String merchant = "{\"account_id\":\"3\",\"name\":\"Dimitris Bisias\",\"username\":\"b1s\",\"password\":\"123456\"}";

        try {
            it.addAccountFromJSON(individual);
            ct.addAccountFromJSON(company);
            mt.addAccountFromJSON(merchant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
