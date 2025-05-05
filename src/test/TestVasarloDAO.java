package test;

import model.Vasarlo;
import model.VasarloDAO;

import java.util.List;

/**
* VasarloDAO osztaly gyors, egyszeru tesztelesere.
*/
public class TestVasarloDAO {
    public static void main(String[] args) {
        VasarloDAO dao = new VasarloDAO();

        // uj vasarlo beilleszt
        Vasarlo newCustomer = new Vasarlo(
                "Teszt Bela",
                "Teszt ter 1, Tesztfalva",
                Vasarlo.Megye.Baranya
        );
        dao.insertCustomer(newCustomer);

        // minden vasarlo listazasa
        List<Vasarlo> customers = dao.getAllCustomers();
        System.out.println("\nAz osszes vasarlo listaja: ");
        for (Vasarlo v : customers) {
            System.out.println(v);
        }

        // az utolso beillesztett vasarlo lehivasa, azonosito alapjan
        if (!customers.isEmpty()) {
            int lastId = customers.get(customers.size() - 1).getVasarloId();
            Vasarlo found = dao.getCustomerById(lastId);
            System.out.println("\nMegtalaltuk a vasarlot az azonosito alapjan " + lastId + ": " + found);

            // tesztvasarlo torlese, eredeti tablaallapot helyreallitasa
            dao.deleteCustomer(lastId);
            System.out.println("A tesztvasarlo sikeresen torolve (azonosito: " + lastId + ")");
        }
    }
}