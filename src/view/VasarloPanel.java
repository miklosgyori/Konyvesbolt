package view;

import model.Vasarlo;
import model.VasarloDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// TODO: dokumentacioba: ismert hiba: a megye a GUI-n szabadon allithato, noha az az adott azonositoju vasarlo eseten
//  az adatbazisban a vasarlo tablaban rogzitett, nem modosithato
/**
 * A vasarlok kezelesere szolgalo GUI panel
 */
public class VasarloPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton loadButton, addButton, editButton, deleteButton;

    public VasarloPanel() {
        setLayout(new BorderLayout());

        // Tabla model
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Azonosito", "Nev", "Cim", "Megye"});

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        loadButton = new JButton("Teljes vasarlolista betoltese");
        addButton = new JButton("Uj vasarlo rogzitese");
        editButton = new JButton("Vasarlo adatainak szerkesztese");
        deleteButton = new JButton("Vasarlo torlese");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadCustomers());
        addButton.addActionListener(e -> addNewCustomer());
        editButton.addActionListener(e -> editCustomerById());
        deleteButton.addActionListener(e -> deleteCustomerById());
    }

    /**
     * Betolti az AB-ben levo vasarlok teljes listajat, megjeleniti a GUI-n.
     */
    private void loadCustomers() {
        tableModel.setRowCount(0);
        VasarloDAO dao = new VasarloDAO();
        List<Vasarlo> customers = dao.getAllCustomers();

        for (Vasarlo v : customers) {
            tableModel.addRow(new Object[]{
                    v.getVasarloId(),
                    v.getNev(),
                    v.getCim(),
                    v.getMegye().toDbValue()
            });
        }

        JOptionPane.showMessageDialog(this,
                customers.size() + " vasarlo betoltve.",
                "A betoltes kesz.",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Uj vasarlot ad az adatbazishoz, a felhasznalo altal a GUI-ban megadott adatokkal.
     */
    private void addNewCustomer() {
        new VasarloFormDialog(null).setVisible(true);
    }

    /**
     * Az adatbazisbol id alapjan kivalasztott vasarlo mezoit modithatja a felhasznalo a GUI-n,
     * a valtozasokat menti az adatbazisban.
     */
    private void editCustomerById() {
        String input = JOptionPane.showInputDialog(this, "Add meg a modositando vasarlo azonositojat:");

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            Vasarlo customer = new VasarloDAO().getCustomerById(id);

            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Nem talaltunk vasarlot ezzel az azonositoval: " + id);
            } else {
                new VasarloFormDialog(customer).setVisible(true);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ervenytelen azonosito formatum.", "Hiba!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Az adatbazisbol id alapjan kivalasztott vasarlot torli az adatbazisbol.
     */
    private void deleteCustomerById() {
        String input = JOptionPane.showInputDialog(this, "Add meg a torlendo vasarlo azonositojat::");

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Biztos, hogy torolni akarod ezt a vasarlot: " + id + "?",
                    "Torles megerositese",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new VasarloDAO().deleteCustomer(id);
                loadCustomers();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ervenytelen azonosito formatum.", "Hiba!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Vasarlo hozzaadasara es modositasara szolgalo FormDialog
     */
    private class VasarloFormDialog extends JDialog {
        private JTextField nevField, cimField;
        private JComboBox<String> megyeBox;
        private JButton saveButton, cancelButton;
        private Vasarlo existing;

        public VasarloFormDialog(Vasarlo existing) {
            this.existing = existing;
            setTitle(existing == null ? "➕ Add Customer" : "📝 Edit Customer");
            setModal(true);
            setLayout(new GridLayout(4, 2, 8, 4));
            setSize(400, 200);
            setLocationRelativeTo(VasarloPanel.this);

            nevField = new JTextField();
            cimField = new JTextField();
            megyeBox = new JComboBox<>();
            for (Vasarlo.Megye m : Vasarlo.Megye.values()) {
                megyeBox.addItem(m.toDbValue());
            }

            if (existing != null) {
                nevField.setText(existing.getNev());
                cimField.setText(existing.getCim());
                megyeBox.setSelectedItem(existing.getMegye().toDbValue());
            }

            saveButton = new JButton("Mentes");
            cancelButton = new JButton("Megse");

            add(new JLabel("Név:")); add(nevField);
            add(new JLabel("Cím:")); add(cimField);
            add(new JLabel("Megye:")); add(megyeBox);
            add(saveButton); add(cancelButton);

            cancelButton.addActionListener(e -> dispose());

            saveButton.addActionListener(e -> {
                try {
                    String nev = nevField.getText().trim();
                    String cim = cimField.getText().trim();
                    String megyeStr = (String) megyeBox.getSelectedItem();
                    Vasarlo.Megye megye = Vasarlo.Megye.fromDbValue(megyeStr);

                    if (nev.isEmpty() || cim.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Név és cím megadása kötelező.");
                        return;
                    }

                    VasarloDAO dao = new VasarloDAO();
                    if (existing == null) {
                        dao.insertCustomer(new Vasarlo(nev, cim, megye));
                    } else {
                        existing.setNev(nev);
                        existing.setCim(cim);
                        existing.setMegye(megye);
                        dao.updateCustomer(existing);
                    }

                    loadCustomers();
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Hiba: " + ex.getMessage());
                }
            });
        }
    }
}
