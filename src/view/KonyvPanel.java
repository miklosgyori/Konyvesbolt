package view;

import model.Konyv;
import model.KonyvDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A konyvek kezelesere szolgalo GUI panel
 */
public class KonyvPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton loadButton;

    public KonyvPanel() {
        setLayout(new BorderLayout());

        // tabla modell
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "ID", "Szerzők", "Cím", "Kiadó", "Kiadás Éve", "Egységár", "Készlet"
        });

        // tabla
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // betoltes gomb
        loadButton = new JButton("Konyvek betoltese");
        loadButton.addActionListener(e -> loadBooks());

        // top panel a gombbal
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadButton);

        // komponensek hozzaadasa
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadBooks() {
        tableModel.setRowCount(0); // clear existing rows

        KonyvDAO dao = new KonyvDAO();
        List<Konyv> konyvek = dao.getAllBooks();

        for (Konyv k : konyvek) {
            Object[] row = new Object[]{
                    k.getKonyvId(),
                    k.getSzerzok(),
                    k.getCim(),
                    k.getKiado(),
                    k.getKiadasEve(),
                    k.getEgysegar(),
                    k.getKeszlet()
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this,
                konyvek.size() + " konyv betoltve.",
                "A betoltes kesz",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
