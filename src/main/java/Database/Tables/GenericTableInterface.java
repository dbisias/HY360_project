package Database.Tables;

import Database.mainClasses.Account;

import java.sql.SQLException;

interface DBTable {

//    public void addNewAccount(Account account) throws SQLException, ClassNotFoundException;
    public void createTable() throws SQLException, ClassNotFoundException;
    public Account findAccount(String username, String password) throws SQLException, ClassNotFoundException;
}