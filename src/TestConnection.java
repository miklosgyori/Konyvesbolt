import model.Database;
import java.sql.Connection;
import java.sql.SQLException;

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
