package test;

import model.Rendeles;
import model.RendelesDAO;
import model.Vasarlo;

import java.time.LocalDate;
import java.util.List;

/**
 * A Rendeles es RendelesDAO osztalyok gyors, egyszeru tesztelesere
 */
public class TestRendelesDAO {
    public static void main(String[] args) {
        RendelesDAO dao = new RendelesDAO();

        // Teszt rendeles beszurasA
        Rendeles tesztRendeles = new Rendeles(
                1, // teszt vasarlo_id — leteznie kell az adatbazisban!!!
                LocalDate.now(),
                Vasarlo.Megye.Baranya, // azonosnak kell lennie az adott vasarlo megyejevel!!!
                Rendeles.Statusz.leadva
        );

        dao.insertOrder(tesztRendeles);

        // Minden rendeles listazasa
        List<Rendeles> rendelesek = dao.getAllOrders();
        System.out.println("Az osszes rendeles:");
        for (Rendeles r : rendelesek) {
            System.out.println(r);
        }

        // Az utolso beszurt rendeles lehivasa id alapjan (feltetelezvem hogy az utolso a listan)
        if (!rendelesek.isEmpty()) {
            Rendeles last = rendelesek.get(rendelesek.size() - 1);
            Rendeles loaded = dao.getOrderById(last.getRendelesId());
            System.out.println("Rendeles betoltve, azonosito: " + loaded);

            // A fenti - feltetelezetten a teszt - rendeles torlese
            dao.deleteOrder(loaded.getRendelesId());
            System.out.println("A kovetkeso azonositoju rendeles sikeresen torolve: " + loaded.getRendelesId());
        } else {
            System.out.println("Nem talaltunk ilyen azonositoval rendelest.");
        }
    }
}

