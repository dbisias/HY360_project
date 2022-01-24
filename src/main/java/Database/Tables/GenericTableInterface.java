package Database.Tables;

import java.sql.SQLException;
import Database.mainClasses.Account;

// TODO: rename DBTable to UserTable

interface DBTable {

//    public void addNewAccount(Account account) throws SQLException, ClassNotFoundException;
    public void createTable() throws SQLException, ClassNotFoundException;
    public void buy(int cli_id, int amount) throws SQLException, ClassNotFoundException;
//    public String accountToJSON(Account account);
    public Account findAccount(String username, String password) throws SQLException, ClassNotFoundException;
}