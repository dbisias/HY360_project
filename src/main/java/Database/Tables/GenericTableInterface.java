package Database.Tables;

import Database.mainClasses.Account;

interface DBTable {

    public void addNewAccount(Account account);
    public void createTable() throws SQLException, ClassNotFoundException;
    public Account findAccount(String username, String password) throws SQLException, ClassNotFoundException;
}