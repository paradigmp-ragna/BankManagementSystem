import java.sql.*;

public class JDBCManager {
    private Connection conn;

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE log (id INTEGER PRIMARY KEY AUTOINCREMENT, action TEXT)");
            System.out.println("Connected to JDBC (in-memory)");
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public void logAction(String action) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO log (action) VALUES (?)");
            ps.setString(1, action);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Logging error: " + e.getMessage());
        }
    }

    public void showLogs() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM log");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("action"));
            }
        } catch (SQLException e) {
            System.out.println("Log fetch error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Close error: " + e.getMessage());
        }
    }
}