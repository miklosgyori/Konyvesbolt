package view;

import model.Rendeles;
import model.RendelesDAO;
import model.Vasarlo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * A rendelesek kezelesere szolgalo GUI panel
 * Ismert hiba: a megye a GUI-n szabadon allithato, noha az az adott azonositoju vasarlo eseten
 * az adatbazisban a vasarlo tablaban rogzitett, nem modosithato, osszetett primary key eleme.
 */
public class RendelesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton loadButton, addButton, editButton, deleteButton;

    public RendelesPanel() {
        setLayout(new BorderLayout());

        // Tabla modell
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Rendeles azonosito", "Vasarlo azonosito", "Datum", "Megye", "Statusz"});

        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Gombok
        loadButton = new JButton("Teljes redneleslista betoltese");
        addButton = new JButton("Uj rendeles rogzitese");
        editButton = new JButton("Rendeles adatainak szerkesztese");
        deleteButton = new JButton("Rendeles torlese");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Esemenykezelok
        loadButton.addActionListener(e -> loadOrders());
        addButton.addActionListener(e -> addNewOrder());
        editButton.addActionListener(e -> editOrderById());
        deleteButton.addActionListener(e -> deleteOrderById());
    }

    /**
     * Betolti az AB-ben levo rendelesek teljes listajat, megjeleniti a GUI-n.
     */
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Rendeles> orders = new RendelesDAO().getAllOrders();
        for (Rendeles r : orders) {
            tableModel.addRow(new Object[]{
                    r.getRendelesId(),
                    r.getVasarloId(),
                    r.getDatum(),
                    r.getMegye().toDbValue(),
                    r.getStatusz().toDbValue()
            });
        }
        JOptionPane.showMessageDialog(this, orders.size() + "  rendeles betoltve.");
    }

    /**
     * Uj rendelest ad az adatbazishoz, a felhasznalo altal a GUI-ban megadott adatokkal.
     */
    private void addNewOrder() {
        new OrderFormDialog(null).setVisible(true);
    }

    /**
     * Az adatbazisbol id alapjan kivalasztott rendeles mezoit modosithatja a felhasznalo a GUI-n,
     * a valtozasokat menti az adatbazisban.
     */
    private void editOrderById() {
        String input = JOptionPane.showInputDialog(this, "Add meg a modositando rendeles azonositojat:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            Rendeles order = new RendelesDAO().getOrderById(id);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Nem talaltunk rendelest ezzel az azonositoval: " + id);
            } else {
                new OrderFormDialog(order).setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ervenytelen azonosito formatum.");
        }
    }

    /**
     * Az adatbazisbol id alapjan kivalasztott rendelest torli az adatbazisbol.
     */
    private void deleteOrderById() {
        String input = JOptionPane.showInputDialog(this, "Add meg a torlendo rendeles azonositojat:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Biztos, hogy torolni akarod ezt a vasarlot: " + id + "?",
                    "Torles megerositese", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new RendelesDAO().deleteOrder(id);
                loadOrders();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ervenytelen azonosito formatum.");
        }
    }

    /**
     * Rendeles hozzaadasara es modositasara szolgalo FormDialog
     */
    private class OrderFormDialog extends JDialog {
        private JTextField vasarloIdField;
        private JComboBox<String> megyeBox, statuszBox;
        private JButton saveButton, cancelButton;
        private Rendeles existing;

        public OrderFormDialog(Rendeles existing) {
            this.existing = existing;
            setTitle(existing == null ? "Rendeles hozzaadasa" : "Rendeles modositasa");
            setModal(true);
            setLayout(new GridLayout(5, 2, 8, 4));
            setSize(400, 220);
            setLocationRelativeTo(RendelesPanel.this);

            vasarloIdField = new JTextField();
            megyeBox = new JComboBox<>();
            statuszBox = new JComboBox<>();

            for (Vasarlo.Megye m : Vasarlo.Megye.values()) {
                megyeBox.addItem(m.toDbValue());
            }

            for (Rendeles.Statusz s : Rendeles.Statusz.values()) {
                statuszBox.addItem(s.toDbValue());
            }

            if (existing != null) {
                vasarloIdField.setText(String.valueOf(existing.getVasarloId()));
                megyeBox.setSelectedItem(existing.getMegye().toDbValue());
                statuszBox.setSelectedItem(existing.getStatusz().toDbValue());
            }

            saveButton = new JButton("💾 Save");
            cancelButton = new JButton("❌ Cancel");

            add(new JLabel("Vasarlo azonosito:")); add(vasarloIdField);
            add(new JLabel("Megye:")); add(megyeBox);
            add(new JLabel("Statusz:")); add(statuszBox);
            add(saveButton); add(cancelButton);

            cancelButton.addActionListener(e -> dispose());

            saveButton.addActionListener(e -> {
                try {
                    long vasarloId = Long.parseLong(vasarloIdField.getText().trim());
                    Vasarlo.Megye megye = Vasarlo.Megye.fromDbValue((String) megyeBox.getSelectedItem());
                    Rendeles.Statusz statusz = Rendeles.Statusz.fromDbValue((String) statuszBox.getSelectedItem());

                    RendelesDAO dao = new RendelesDAO();
                    if (existing == null) {
                        dao.insertOrder(new Rendeles(vasarloId, LocalDate.now(), megye, statusz));
                    } else {
                        existing.setVasarloId(vasarloId);
                        existing.setMegye(megye);
                        existing.setStatusz(statusz);
                        dao.updateOrder(existing);
                    }

                    loadOrders();
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Hiba: " + ex.getMessage());
                }
            });
        }
    }
}

