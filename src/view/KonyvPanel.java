package view;

import model.Konyv;
import model.KonyvDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
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
                "ID", "Szerzok", "Cim", "Kiado", "Kiadas eve", "Egysegar", "Keszlet"
        });

        // tabla
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
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
     * Betolti az AB-ben levo konyvek teljes listajat, megjeleniti a GUI-n.
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

    /**
     * Uj konyvet ad az adatbazishoz, a felhasznalo altal a GUI-ban megadott adatokkal.
     */
    private void addNewBook() {
        new BookFormDialog(null).setVisible(true);
    }

    /**
     * Az adatbazisbol id alapjan kivalasztott konyv mezoit modithatja a felhasznalo a GUI-n,
     * a valtozasokat menti az adatbazisban.
     */
    private void editSelectedBook() {
        String input = JOptionPane.showInputDialog(this, "Add meg a modositando konyv azonositojat");

        if (input == null || input.trim().isEmpty()) {
            return; // Cancelled or empty input
        }

        try {
            int id = Integer.parseInt(input.trim());

            KonyvDAO dao = new KonyvDAO();
            Konyv book = dao.getBookById(id);

            if (book == null) {
                JOptionPane.showMessageDialog(this, "Nem talaltunk konyvet ezzel az azonositoval: " + id,
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new BookFormDialog(book).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ervenytelen azonosito formatum.", "Hiba!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Id-val azonositott konyvet torol az AB-bol, megerosites utan.
     */
    private void deleteSelectedBook() {
        String input = JOptionPane.showInputDialog(this, "Add meg a torlendo konyv azonositojat:");

        if (input == null || input.trim().isEmpty()) {
            return; // Cancelled or empty
        }

        try {
            int id = Integer.parseInt(input.trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Biztos, hogy torolni akarod ezt a konyvet: " + id + "?",
                    "Torles megerositese",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // Cancelled
            }

            KonyvDAO dao = new KonyvDAO();
            dao.deleteBook(id);
            loadBooks(); // refresh the table

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ervenytelen azonosito formatum.",
                    "Hiba!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cim vagy cim toredeke alapjan keres konyvet, megjeleniti a GUI-n.
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

     /**
     * Belso osztaly: input form definialas; a megadott adatokkal uj konyv hozzaadasa az AB-hoz VAGY letezo konyv
     * mezoinek szerkesztese.
     */
    private class BookFormDialog extends JDialog {
        private JTextField szerzokField, cimField, kiadoField, evField, arField, keszletField;
        private JButton saveButton, cancelButton;
        private Konyv existingBook;

        public BookFormDialog(Konyv bookToEdit) {
            this.existingBook = bookToEdit;
            setTitle(bookToEdit == null ? "Uj konyv hozzaadasa" : "Konyv adatainak szerkesztese");
            setModal(true);
            setLayout(new GridLayout(7, 2, 8, 4));
            setSize(400, 300);
            setLocationRelativeTo(KonyvPanel.this);

            // Fields
            szerzokField = new JTextField();
            cimField = new JTextField();
            kiadoField = new JTextField();
            evField = new JTextField();
            arField = new JTextField();
            keszletField = new JTextField();

            // Pre-fill if editing
            if (bookToEdit != null) {
                szerzokField.setText(bookToEdit.getSzerzok());
                cimField.setText(bookToEdit.getCim());
                kiadoField.setText(bookToEdit.getKiado());
                if (bookToEdit.getKiadasEve() != null) {
                    evField.setText(String.valueOf(bookToEdit.getKiadasEve().getYear()));
                }
                arField.setText(String.valueOf(bookToEdit.getEgysegar()));
                keszletField.setText(String.valueOf(bookToEdit.getKeszlet()));
            }

            // Buttons
            saveButton = new JButton("Mentes");
            cancelButton = new JButton("Megse");

            // Layout
            add(new JLabel("Szerzok:")); add(szerzokField);
            add(new JLabel("Cim:*"));    add(cimField);
            add(new JLabel("Kiado:"));   add(kiadoField);
            add(new JLabel("Kiadas eve:")); add(evField);
            add(new JLabel("Egysegar:*"));  add(arField);
            add(new JLabel("Keszlet:*"));   add(keszletField);
            add(saveButton); add(cancelButton);

            cancelButton.addActionListener(e -> dispose());

            saveButton.addActionListener(e -> {
                try {
                    String szerzok = szerzokField.getText().trim();
                    String cim = cimField.getText().trim();
                    String kiado = kiadoField.getText().trim();
                    String ev = evField.getText().trim();
                    String ar = arField.getText().trim();
                    String keszlet = keszletField.getText().trim();

                    if (cim.isEmpty() || ar.isEmpty() || keszlet.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Cim, egysegar es keszlet megadasa kotelezo!");
                        return;
                    }

                    int egysegar = Integer.parseInt(ar);
                    short keszletValue = Short.parseShort(keszlet);
                    LocalDate kiadasEve = ev.isEmpty() ? null : LocalDate.of(Integer.parseInt(ev), 1, 1);

                    Konyv book = new Konyv(
                            existingBook != null ? existingBook.getKonyvId() : 0,
                            szerzok, cim, kiado, kiadasEve, egysegar, keszletValue
                    );

                    KonyvDAO dao = new KonyvDAO();
                    if (existingBook == null) {
                        dao.insertBook(book);
                    } else {
                        dao.updateBook(book);
                    }

                    loadBooks();
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Hiba: " + ex.getMessage(), "Hibas adat", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
}
