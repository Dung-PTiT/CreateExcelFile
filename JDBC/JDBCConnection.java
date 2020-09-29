package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

    private final String HOST = "localhost";
    private final String USERNAME = "root";
    private final String PASSWORD = "12345";
    private final String DATABASE = "excel";
    private final String PORT = "3306";
    private final String CLASSNAME = "com.mysql.jdbc.Driver";
    private final String URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE);

    private Connection connection = null;

    private JDBCConnection() {
        try {
            Class.forName(CLASSNAME);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static JDBCConnection getInstance() {
        return JDBCConnectionProviderHolder.INSTANCE;
    }

    private static class JDBCConnectionProviderHolder {

        private static final JDBCConnection INSTANCE = new JDBCConnection();
    }
}
