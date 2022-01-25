package Database.Tables;

import java.sql.SQLException;
import Database.mainClasses.Account;
import Exceptions.InsufficientBalanceException;
import Exceptions.UserNotFoundException;

// TODO: rename DBTable to UserTable

interface DBTable {

//    public void addNewAccount(Account account) throws SQLException, ClassNotFoundException;
    public void createTable() throws SQLException, ClassNotFoundException;
    public void delAccount(int acc_id) throws SQLException, ClassNotFoundException;
    public int buy(int cli_id, int mer_id, double amount) throws ClassNotFoundException, SQLException;
//    public String accountToJSON(Account account);
    public Account findAccount(String username, String password) throws SQLException, ClassNotFoundException;
}