package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Object Access osztaly, a Rendeles osztaly es a 'rendeles' AB tabla kozotti interakciokra
 */
public class RendelesDAO {

    /**
     * lekerdezi az osszes rendelest az AB-bol
     * @return ArrayList<Rendeles> rendelesek
     */
    public List<Rendeles> getAllOrders() {
        List<Rendeles> rendelesek = new ArrayList<>();
        String sql = "SELECT * FROM konyvesbolt.rendeles ORDER BY rendeles_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rendeles r = new Rendeles(
                        rs.getInt("rendeles_id"),
                        rs.getLong("vasarlo_id"),
                        rs.getDate("datum").toLocalDate(),
                        Vasarlo.Megye.fromDbValue(rs.getString("megye")),
                        Rendeles.Statusz.fromDbValue(rs.getString("statusz"))
                );
                rendelesek.add(r);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching orders: " + e.getMessage());
        }

        return rendelesek;
    }

    /**
     * uj vasarlot szur be az AB-ba (vasarlo tablaba)
     * @param rendeles A beszurando Rendeles objektum
     */
    public void insertOrder(Rendeles rendeles) {
        String sql = """
                INSERT INTO konyvesbolt.rendeles (vasarlo_id, datum, megye, statusz)
                VALUES (?, ?, ?::konyvesbolt.county, ?::konyvesbolt.order_state)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, rendeles.getVasarloId());
            stmt.setDate(2, Date.valueOf(rendeles.getDatum()));
            stmt.setString(3, rendeles.getMegye().toDbValue());
            stmt.setString(4, rendeles.getStatusz().toDbValue());

            stmt.executeUpdate();
            System.out.println("Rendeles sikeresen beszurva.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a rendeles beillesztesenel: " + e.getMessage());
        }
    }

    /**
     * Egy, mar az AB-ban levo rendeles adatait  frissiti
     * @param rendeles A modositando Rendeles objektum
     */
    public void updateOrder(Rendeles rendeles) {
        String sql = """
                UPDATE konyvesbolt.rendeles
                SET vasarlo_id = ?, datum = ?, megye = ?::konyvesbolt.county, statusz = ?::konyvesbolt.order_state
                WHERE rendeles_id = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, rendeles.getVasarloId());
            stmt.setDate(2, Date.valueOf(rendeles.getDatum()));
            stmt.setString(3, rendeles.getMegye().toDbValue());
            stmt.setString(4, rendeles.getStatusz().toDbValue());
            stmt.setInt(5, rendeles.getRendelesId());

            stmt.executeUpdate();
            System.out.println("Rendeles adatai sikeresen modositva.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a rendeles frissitesenel: " + e.getMessage());
        }
    }

    /**
     * Egy rendelest torol az AB-bol, annak ID-ja alapjan
     * @param rendelesID A torlendo rendeles ID-ja.
     */
    public void deleteOrder(int rendelesID) {
        String sql = "DELETE FROM konyvesbolt.rendeles WHERE rendeles_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rendelesID);
            stmt.executeUpdate();
            System.out.println("🗑️ Order deleted.");

        } catch (SQLException e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
        }
    }

    /**
     * id alapjan kikeres es visszaad egy rendelest.
     * @param rendelesID, a keresendo rendeles id-je
     * @return vasarlo vs null (ha a parameterkent megadott id-val nem letezik vasarlo az AB-ben)
     */
    public Rendeles getOrderById(int rendelesID) {
        String sql = "SELECT * FROM konyvesbolt.rendeles WHERE rendeles_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rendelesID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Rendeles(
                        rs.getInt("rendeles_id"),
                        rs.getLong("vasarlo_id"),
                        rs.getDate("datum").toLocalDate(),
                        Vasarlo.Megye.fromDbValue(rs.getString("megye")),
                        Rendeles.Statusz.fromDbValue(rs.getString("statusz"))
                );
            }

        } catch (SQLException e) {
            System.err.println("Hiba a rendeles azonosito alapjan torteno lehivasaban: " + e.getMessage());
        }

        return null;
    }
}
