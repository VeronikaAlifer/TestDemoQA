package utils;

import java.sql.*;

public class DBTest {

    public static void dbConnection() {
        String url = "jdbc:mysql://localhost:3306/selenium";
        String user = "root";
        String password = "newPassword123";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Подключение к БД успешно");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                System.out.println("User: " + rs.getString("name") + ", Age: " + rs.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
