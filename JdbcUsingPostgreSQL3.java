import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class JdbcUsingPostgreSQL3 {

    private static Scanner scanner;

    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            String url = properties.getProperty("POSTGRES_URL");
            String username = properties.getProperty("POSTGRES_USERNAME");
            String password = properties.getProperty("POSTGRES_PASSWORD");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                init(connection);

                scanner = new Scanner(System.in);
                int choice;
                do {
                    System.out.println("Select an operation:");
                    System.out.println("1. Insert");
                    System.out.println("2. Update");
                    System.out.println("3. Delete");
                    System.out.println("4. Select");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice (1-5): ");
                    choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            executeInsert(connection);
                            break;
                        case 2:
                            executeUpdate(connection);
                            break;
                        case 3:
                            executeDelete(connection);
                            break;
                        case 4:
                            executeSelect(connection);
                            break;
                        case 5:
                            System.out.println("Exiting the program.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                } while (choice != 5);
            } catch (SQLException e) {
                System.err.println("SQL Exception: " + e.getMessage());
            }
        } catch (IOException e){
            System.out.println("IO Exception: " + e.getMessage());
        } catch (InputMismatchException e){
            System.out.println("InputMismatch Exception: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void init(Connection connection) throws SQLException {

        scanner = new Scanner(System.in);

        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees ( " +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "age INTEGER, " +
                "salary DECIMAL(10, 2) " +
                ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSQL);
    }

    private static void executeInsert(Connection connection) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter salary: ");
        int salary = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String sqlQuery = "INSERT INTO employees (name, salary) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, salary);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }

    private static void executeUpdate(Connection connection) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter new salary: ");
        int newSalary = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String sqlQuery = "UPDATE employees SET salary = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, newSalary);
            preparedStatement.setInt(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    private static void executeDelete(Connection connection) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String sqlQuery = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        }
    }

    private static void executeSelect(Connection connection) throws SQLException {
        String sqlQuery = "SELECT * FROM employees";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Name: " + resultSet.getString("name") +
                        ", Salary: " + resultSet.getInt("salary"));
            }
        }
    }
}
