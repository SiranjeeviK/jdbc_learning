import java.sql.*;

public class JdbcUsingXampp  {
    //    connect with xammp
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/ajp";
        String username = "root";
        String password = "";

        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            con.close();
        }

    }
}
