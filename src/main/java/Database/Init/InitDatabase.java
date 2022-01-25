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
        String company1 = "{\"name\":\"CCC\",\"username\":\"ccc\",\"password\":\"123456\"}";
        String company2 = "{\"name\":\"Amita\",\"username\":\"amita\",\"password\":\"amita\"}";
        String company3 = "{\"name\":\"RedBull\",\"username\":\"redbull\",\"password\":\"redbull\"}";
        String company4 = "{\"name\":\"Black Diamond\",\"username\":\"bdiamond\",\"password\":\"bdiamond\"}";
        String company5 = "{\"name\":\"Suzuki\",\"username\":\"suzuki\",\"password\":\"samurai\"}";

        String individual1 = "{\"name\":\"Christos Papastamos\",\"username\":\"papastam\",\"password\":\"123456\", \"company_account_id\":1}";
        String individual2 = "{\"name\":\"Dimitris Bisias\",\"username\":\"b1s_\",\"password\":\"654321\", \"company_account_id\":2}";;
        String individual3 = "{\"name\":\"Orestis Chiotakis\",\"username\":\"chiotakos\",\"password\":\"456123\", \"company_account_id\":3}";;
        String individual4 = "{\"name\":\"Kyriakos Fragkakos\",\"username\":\"magkas7\",\"password\":\"faih123\"}";;
        String individual5 = "{\"name\":\"Nwntas Kartsonakis\",\"username\":\"moun10\",\"password\":\"tovou123\"}";;


        String merchant1 = "{\"name\":\"Georgios Lamprinakis\",\"username\":\"modz123\",\"password\":\"ergo123\"}";
        String merchant2 = "{\"name\":\"Antonis Dhmhtrakopoulos\",\"username\":\"AD\",\"password\":\"metwn12.5\"}";
        String merchant3 = "{\"name\":\"Shfhs Gyparhs\",\"username\":\"jgyparhs\",\"password\":\"shotgunbr0\"}";
        String merchant4 = "{\"name\":\"Giannis Smyrn\",\"username\":\"smyrn\",\"password\":\"kafes\"}";
        String merchant5 = "{\"name\":\"Giannis Oiko\",\"username\":\"gangsterOiko\",\"password\":\"kerdisaIphone\"}";


        try {
            ct.addAccountFromJSON(company1);
            ct.addAccountFromJSON(company2);
            ct.addAccountFromJSON(company3);
            ct.addAccountFromJSON(company4);
            ct.addAccountFromJSON(company5);
            it.addAccountFromJSON(individual1);
            it.addAccountFromJSON(individual2);
            it.addAccountFromJSON(individual3);
            it.addAccountFromJSON(individual4);
            it.addAccountFromJSON(individual5);
            mt.addAccountFromJSON(merchant1);
            mt.addAccountFromJSON(merchant2);
            mt.addAccountFromJSON(merchant3);
            mt.addAccountFromJSON(merchant4);
            mt.addAccountFromJSON(merchant5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
