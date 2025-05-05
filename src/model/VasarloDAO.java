package model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Object Access osztaly, a Vasarlo osztaly es a vasarlo AB tabla kozotti interakciokra
 */
public class VasarloDAO {

    /**
     * lekerdezi az osszes vasarlot az AB-bol
     * @return ArrayList<Vasarlo> vasarlok
     */
    public List<Vasarlo> getAllCustomers() {
        List<Vasarlo> vasarlok = new ArrayList<>();
        String sql = "SELECT * FROM konyvesbolt.vasarlo ORDER BY vasarlo_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vasarlo v = new Vasarlo(
                        rs.getInt("vasarlo_id"),
                        rs.getString("nev"),
                        rs.getString("cim"),
                        Vasarlo.Megye.fromDbValue(rs.getString("megye"))
                );
                vasarlok.add(v);
            }

        } catch (SQLException e) {
            System.err.println("!!! Hiba a vasarlok lekeresenel: " + e.getMessage());
        }

        return vasarlok;
    }

    /**
     * uj vasarlot szur be az AB-ba (vasarlo tablaba)
     * @param vasarlo A beszurando Vasarlo objektum
     */
    public void insertCustomer(Vasarlo vasarlo) {
        String sql = "INSERT INTO konyvesbolt.vasarlo (nev, cim, megye) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vasarlo.getNev());
            stmt.setString(2, vasarlo.getCim());
            stmt.setString(3, vasarlo.getMegye().toDbValue());

            stmt.executeUpdate();
            System.out.println("Vasarlo sikeresen beillesztve.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a vasarlo beillesztesenel: " + e.getMessage());
        }
    }

    /**
     * Egy, mar az AB-ban levo vasarlo adatait  frissiti
     * @param vasarlo A modositando Vasarlo objektum
     */
    public void updateCustomer(Vasarlo vasarlo) {
        String sql = "UPDATE konyvesbolt.vasarlo SET nev = ?, cim = ?, megye = ? WHERE vasarlo_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vasarlo.getNev());
            stmt.setString(2, vasarlo.getCim());
            stmt.setString(3, vasarlo.getMegye().toDbValue());
            stmt.setInt(4, vasarlo.getVasarloId());

            stmt.executeUpdate();
            System.out.println("Vasarlo adatai sikeresen modositva.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a vasarlo adatainak modositasa soran: " + e.getMessage());
        }
    }

    /**
     * Egy vasarlot torol az AB-bol, annak ID-ja alapjan
     * @param vasarloId A torlendo vasarlo ID-ja.
     */
    public void deleteCustomer(int vasarloID) {
        String sql = "DELETE FROM konyvesbolt.vasarlo WHERE vasarlo_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vasarloID);
            stmt.executeUpdate();
            System.out.println("Vasarlo sikeresen torolve.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a vasarlo torlesenel: " + e.getMessage());
        }
    }

    /**
     * id alapjan kikeres es visszaad egy vasarlot.
     * @param vasarloID, a keresendo konyv id-je
     * @return vasarlo vs null (ha a parameterkent megadott id-val nem letezik vasarlo az AB-ben)
     */
    public Vasarlo getCustomerById(int vasarloID) {
        String sql = "SELECT * FROM konyvesbolt.vasarlo WHERE vasarlo_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vasarloID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vasarlo(
                        rs.getInt("vasarlo_id"),
                        rs.getString("nev"),
                        rs.getString("cim"),
                        Vasarlo.Megye.fromDbValue(rs.getString("megye"))
                );
            }

        } catch (SQLException e) {
            System.err.println("Hiba a vasarlo azonosito alapjan torteno lehivasaban: " + e.getMessage());
        }

        return null;
    }
}
