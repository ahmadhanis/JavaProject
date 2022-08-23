import java.sql.*;


public class Connect {
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            
            String url = "jdbc:sqlite:C:/Users/DELL/Documents/GitHub/JavaProject/foodapp_sqlite/src/chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }

}
