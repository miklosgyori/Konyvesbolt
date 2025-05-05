package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Object Access osztaly, a Konyv osztaly es a konyv AB tabla kozotti interakciokra
 * Ismert korlat: hibauzenetek egy reszet csak a konzolra irja ki.
 */
public class KonyvDAO {

    /**
     * lekerdezi az osszes konyvet az AB-bol
     * @return konyvek
     */
    public List<Konyv> getAllBooks() {
        List<Konyv> konyvek = new ArrayList<>();
        String sql = "SELECT * FROM konyvesbolt.konyv ORDER BY konyv_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Konyv k = new Konyv(
                        rs.getInt("konyv_id"),
                        rs.getString("szerzok"),
                        rs.getString("cim"),
                        rs.getString("kiado"),
                        rs.getDate("kiadas_eve") != null ? rs.getDate("kiadas_eve").toLocalDate() : null,
                        rs.getInt("egysegar"),
                        rs.getShort("keszlet")
                );
                konyvek.add(k);
            }

        } catch (SQLException e) {
             System.err.println("!!! Hiba a konyvek lekeresenel: " + e.getMessage());
        }

        return konyvek;
    }

    /**
     * uj konyvet szur be az AB-ba (konyv tablaba)
     * @param k A beszurando konyv objektum
     */
    public void insertBook(Konyv k) {
        String sql = "INSERT INTO konyvesbolt.konyv (szerzok, cim, kiado, kiadas_eve, egysegar, keszlet) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, k.getSzerzok());
            stmt.setString(2, k.getCim());
            stmt.setString(3, k.getKiado());
            if (k.getKiadasEve() != null) {
                stmt.setDate(4, Date.valueOf(k.getKiadasEve()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, k.getEgysegar());
            stmt.setShort(6, k.getKeszlet());

            stmt.executeUpdate();
            System.out.println("A konyv sikeresen beillesztve az adatbazisba");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a konyv beillesztesenel: " + e.getMessage());
        }
    }

    /**
     * Egy, mar az Ab-ban levo konyv adatait  frissiti
     * @param k A frissitendo Konyv objektum
     */
    public void updateBook(Konyv k) {
        String sql = "UPDATE konyvesbolt.konyv SET szerzok=?, cim=?, kiado=?, kiadas_eve=?, egysegar=?, keszlet=? WHERE konyv_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, k.getSzerzok());
            stmt.setString(2, k.getCim());
            stmt.setString(3, k.getKiado());
            if (k.getKiadasEve() != null) {
                stmt.setDate(4, Date.valueOf(k.getKiadasEve()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, k.getEgysegar());
            stmt.setShort(6, k.getKeszlet());
            stmt.setInt(7, k.getKonyvId());

            stmt.executeUpdate();
            System.out.println("A konyv adatai sikeresen frissitve az adatbazisban.");

        } catch (SQLException e) {
            System.err.println("!!! Hiba a konyv adatainak frissitesenel:  " + e.getMessage());
        }
    }

    /**
     * Egy konyvet torol az AB-bol, annak ID-ja alapjan
     * @param konyvId A torlendo konyv ID-ja.
     */
    public void deleteBook(int konyvId) {
        String sql = "DELETE FROM konyvesbolt.konyv WHERE konyv_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, konyvId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Konyv sikeresen torolve.");
            } else {
                System.out.println("!!! Nem letezik konyv ezzel az azonositoval.");
            }

        } catch (SQLException e) {
            System.err.println("!!! Hiba a konyv torlesenel: " + e.getMessage());
        }
    }

    /**
     * Konyveket keres az adatbazisban egy cim alapjan
     * @param keresettCim A keresett konyv(ek) cime
     * @return talalatok A megadott cimmel talalt konyvek listaja
     */
    public List<Konyv> searchBooksByTitle(String keresettCim) {
        List<Konyv> talalatok = new ArrayList<>();
        String sql = "SELECT * FROM konyvesbolt.konyv WHERE LOWER(cim) LIKE LOWER(?) ORDER BY konyv_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keresettCim + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Konyv k = new Konyv(
                            rs.getInt("konyv_id"),
                            rs.getString("szerzok"),
                            rs.getString("cim"),
                            rs.getString("kiado"),
                            rs.getDate("kiadas_eve") != null ? rs.getDate("kiadas_eve").toLocalDate() : null,
                            rs.getInt("egysegar"),
                            rs.getShort("keszlet")
                    );
                    talalatok.add(k);
                }
            }

        } catch (SQLException e) {
            System.err.println("!!! Hiba a cim alapjan torteno kereses soran: " + e.getMessage());
        }

        return talalatok;
    }

    /**
     * id alapjan kikeres es visszaad egy konyvet.
     * @param id, a keresendo konyv id-je
     * @return konyv vs null (ha a parameterkent megadott id-val nem letezik konyv az AB-ben)
     */
    public Konyv getBookById(int id) {
        String sql = "SELECT * FROM konyvesbolt.konyv WHERE konyv_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Konyv(
                        rs.getInt("konyv_id"),
                        rs.getString("szerzok"),
                        rs.getString("cim"),
                        rs.getString("kiado"),
                        rs.getDate("kiadas_eve") != null ? rs.getDate("kiadas_eve").toLocalDate() : null,
                        rs.getInt("egysegar"),
                        rs.getShort("keszlet")
                );
            }

        } catch (SQLException e) {
            System.err.println("Hiba a konyv azonosito alapjan torteno lehivasaban: " + e.getMessage());
        }

        return null;
    }
}
