package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A postgres adatbazissal valo kapcsolat definialasa, a kapcsolat letrehozasa es lezarasa, kivetelkezelessel.
 */
public class Database {

    // Database connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5488/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static Connection connection = null;

    /**
     * A kapcsolat letrehozasa, kivetelkezelessel.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // PostgreSQL JDBC driver betoltese
                Class.forName("org.postgresql.Driver");
                // Establish the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Sikeres kapcsolodas a PostgreSQL adatbazishoz.");
            } catch (ClassNotFoundException e) {
                System.err.println("!!! PostgreSQL JDBC driver nem talalhato.");
                throw new SQLException(e);
            } catch (SQLException e) {
                System.err.println("!!! Nem sikerult kapcsolodni az adatbazishoz.");
                throw e;
            }
        }
        return connection;
    }

    /**
     * A kapcsolat "manualis" lezarasa, kivetelkezelessel.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Adatbazis kapcsolat sikeresen bezarva.");
            }
        } catch (SQLException e) {
            System.err.println("!!! Nem sikerult bezarni a kapcsolatot az adatbazissal: " + e.getMessage());
        }
    }
}
