package ws.GamesNetworkSoap.Server;

import ws.GamesNetworkSoap.exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @desc A singleton database access class for MySQL
 * @author Ramindu
 */
public final class DatabaseConnection {
    public Connection conn;
    private Statement statement;
    public static DatabaseConnection db;
    private DatabaseConnection() throws ConnectionException {
        String url= "jdbc:postgresql://localhost:5432/";
        String dbName = "GamesNetwork";
        String driver = "org.postgresql.Driver";
        String userName = "postgres";
        String password = "postgres";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
            throw new ConnectionException("Hubo un error de conexión con la base de datos");
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized DatabaseConnection getDbCon() throws ConnectionException {
        if ( db == null ) {
            db = new DatabaseConnection();
        }
        return db;
 
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;
 
    }
    
    public int update(String insertQuery) throws SQLException {
        
    	return this.insert(insertQuery);
 
    }
    
    public int delete(String deleteQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(deleteQuery);
        return result;
 
    }
 
}