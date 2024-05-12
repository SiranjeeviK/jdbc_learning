import java.sql.*;
public class JdbcUsingPostgreSQL {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "siran1234";

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from hehe");
            while(resultSet.next()){
                System.out.println(resultSet.getString("name"));
            }

            
            resultSet.close();
            statement.close();
            connection.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } finally {
            
        }
    }
}
