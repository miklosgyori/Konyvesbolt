import model.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            Connection connection = Database.getConnection();
            Database.closeConnection();
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
    }
}
