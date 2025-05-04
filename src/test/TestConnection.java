package test;

import model.Database;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A postgres adatbazissal valo kapcsolat gyors es egyszeru tesztelesere.
 */
public class TestConnection {
    public static void main(String[] args){
        try {
            Connection connection = Database.getConnection();
            Database.closeConnection();
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
    }
}
