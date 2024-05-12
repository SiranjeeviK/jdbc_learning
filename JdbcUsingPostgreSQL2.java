import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JdbcUsingPostgreSQL2 {
    public static void main(String[] args) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            String url = properties.getProperty("POSTGRES_URL");
            String username = properties.getProperty("POSTGRES_USERNAME");
            String password = properties.getProperty("POSTGRES_PASSWORD");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String tableName = "hehe";
//                String sqlQuery = "SELECT * FROM " + tableName;
                String sqlQuery = "truncate " + tableName;

                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString("name"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getSQLState()+" "+ e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }
}
