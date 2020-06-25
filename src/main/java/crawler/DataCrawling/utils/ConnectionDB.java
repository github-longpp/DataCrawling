package crawler.DataCrawling.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {
    // Kết nối vào MySQL.
    public static Connection getMySQLConnection() {
            String hostName = "localhost";
            String dbName = "test";
            String userName = "root";
            String password = "111111";

        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password) {
        Connection conn = null ;
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            // Cấu trúc URL Connection dành cho Oracle
            // Ví dụ: jdbc:mysql://localhost:3306/simplehr
            String url = "jdbc:mariadb://" + hostName + ":3306/" + dbName;

            conn = DriverManager.getConnection(url, userName,
                    password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
