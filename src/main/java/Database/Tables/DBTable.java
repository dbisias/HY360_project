
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBTable<T> {
    
    private Gson gson;
    private ResultSet rs;
    private Connection con;
    private Statement stmt;


    public DBTable() {

        gson = new Gson();
    }
    
    protected abstract void addTupleFromJSON(String json) throws SQLException, ClassNotFoundException;
    protected abstract void createTable(T obj) throws SQLException, ClassNotFoundException;
    protected abstract void addTuple(T obj) throws SQLException, ClassNotFoundException;
    protected abstract void delTuple(int key) throws SQLException, ClassNotFoundException;
    protected abstract void updTuple() throws SQLException, ClassNotFoundException;
    protected abstract T getTuple(int key) throws SQLException, ClassNotFoundException;
    protected abstract boolean existsTuple(int key) throws SQLException, ClassNotFoundException;
}
