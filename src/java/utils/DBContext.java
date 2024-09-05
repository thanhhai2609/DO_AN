package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static String jdbcURL = "jdbc:mysql://localhost:3306/clothesshop?useSSL=false&allowPublicKeyRetrieval=true";
    private static String jdbcUsername = "root";
    private static String jdbcPassword = "123456";
    
    public static Connection getConnection() {
        Connection connection = null;
    
        try {        
            Class.forName("com.mysql.cj.jdbc.Driver");        
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return connection;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
