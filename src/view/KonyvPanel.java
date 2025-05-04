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
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField searchField;

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

        // gombok
        loadButton = new JButton("Teljes konyvlista betoltese");
        loadButton.addActionListener(e -> loadBooks());

        addButton = new JButton("Uj konyv rogzitese");
        addButton.addActionListener(e -> addNewBook());

        editButton = new JButton("Konyv adatainak szerkesztese");
        editButton.addActionListener(e -> editSelectedBook());

        deleteButton = new JButton("Konyv torlese");
        deleteButton.addActionListener(e -> deleteSelectedBook());

        searchButton = new JButton("Konyv keresese cim/cimtoredek alapjan");
        searchButton.addActionListener(e -> searchBooks());

        searchField = new JTextField(20);


        // top panel a gombokkal
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // komponensek hozzaadasa
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Betolti az AB-ben levo konyvek teljes listajat.
     */
    private void loadBooks() {
        tableModel.setRowCount(0);

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

    private void addNewBook() {
        JOptionPane.showMessageDialog(this, "Add book form not yet implemented.");
    }

    private void editSelectedBook() {
        JOptionPane.showMessageDialog(this, "Edit book form not yet implemented.");
    }

    private void deleteSelectedBook() {
        JOptionPane.showMessageDialog(this, "Delete functionality not yet implemented.");
    }

    /**
     * Cim vagy cim toredeke alapjan keres konyvet
     */
    private void searchBooks() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Add meg a cimet vagy a cim egy reszet!",
                    "Keresokifejezes szukseges!",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        KonyvDAO dao = new KonyvDAO();
        List<Konyv> results = dao.searchBooksByTitle(keyword);

        tableModel.setRowCount(0); // Clear table before showing new results

        for (Konyv k : results) {
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

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nem talalhato konyv ezzel a cimmel/cimtoredekkel: \"" + keyword + "\".",
                    "Nincs talalat!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
