package test;

import model.Konyv;
import model.KonyvDAO;

import java.time.LocalDate;
import java.util.List;

/**
 * KonyvDAO osztaly gyors, egyszeru tesztelesere.
 */
public class TestKonyvDAO {
    public static void main(String[] args) {
        KonyvDAO dao = new KonyvDAO();

        // uj konyv beszurasa
        Konyv ujKonyv = new Konyv(
                "UjSzerzo UjSzerzo",
                "UjCim",
                "UjKiado, UjVaros",
                LocalDate.of(2000, 1, 1),
                2025,  // egysegar
                (short) 2025  // keszlet
        );
        dao.insertBook(ujKonyv);

        // minden konyv kilistazasa
        List<Konyv> konyvek = dao.getAllBooks();
        System.out.println("\nAz osszes konyv listaja: ");
        for (Konyv konyv : konyvek) {
            System.out.println(konyv);
        }


        // konyv keresese cim alapjan
        List<Konyv> talalatok = dao.searchBooksByTitle("UjCim");
        System.out.println("\nTalalatok az 'UjCim' konyvcimre: ");
        for (Konyv konyv : talalatok) {
            System.out.println(konyv);
        }

        // a fenti lista 1. elemenek - alapertelmezesben a most beszurt konvnek a torlese az AB-bol,
        // ezzel a teszteles elotti AB tartalom visszaallitasa.
        if (!talalatok.isEmpty()) {
            int idToDelete = talalatok.get(0).getKonyvId();
            dao.deleteBook(idToDelete);
            System.out.println("\nA kovetkezo ID-ju konyv sikeresen torolve: " + idToDelete);
        }
    }
}