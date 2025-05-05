package view;

import model.Rendeles;
import model.RendelesDAO;
import model.Vasarlo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

// TODO: JAVADoc, egyeb annotaciok, kommunikacio magyaritasa, ha teszteles OK.
// TODO: dokumentacioba: ismert hiba: a megye a GUI-n szabadon allithato, noha az az adott azonositoju vasarlo eseten
//  az adatbazisban a vasarlo tablaban rogzitett, nem modosithato

/**
 * A rendelesek kezelesere szolgalo GUI panel
 */
public class RendelesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton loadButton, addButton, editButton, deleteButton;

    public RendelesPanel() {
        setLayout(new BorderLayout());

        // Tabla modell
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Vásárló ID", "Dátum", "Megye", "Státusz"});

        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        loadButton = new JButton("📥 Load");
        addButton = new JButton("➕ Add");
        editButton = new JButton("📝 Edit");
        deleteButton = new JButton("🗑 Delete");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(loadButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        loadButton.addActionListener(e -> loadOrders());
        addButton.addActionListener(e -> addNewOrder());
        editButton.addActionListener(e -> editOrderById());
        deleteButton.addActionListener(e -> deleteOrderById());
    }

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
        JOptionPane.showMessageDialog(this, orders.size() + " order(s) loaded.");
    }

    private void addNewOrder() {
        new OrderFormDialog(null).setVisible(true);
    }

    private void editOrderById() {
        String input = JOptionPane.showInputDialog(this, "Enter the order ID to edit:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            Rendeles order = new RendelesDAO().getOrderById(id);
            if (order == null) {
                JOptionPane.showMessageDialog(this, "No order with ID: " + id);
            } else {
                new OrderFormDialog(order).setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        }
    }

    private void deleteOrderById() {
        String input = JOptionPane.showInputDialog(this, "Enter the order ID to delete:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete order with ID " + id + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new RendelesDAO().deleteOrder(id);
                loadOrders();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        }
    }

    // Inner dialog class
    private class OrderFormDialog extends JDialog {
        private JTextField vasarloIdField;
        private JComboBox<String> megyeBox, statuszBox;
        private JButton saveButton, cancelButton;
        private Rendeles existing;

        public OrderFormDialog(Rendeles existing) {
            this.existing = existing;
            setTitle(existing == null ? "Add Order" : "Edit Order");
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

            add(new JLabel("Vásárló ID:")); add(vasarloIdField);
            add(new JLabel("Megye:")); add(megyeBox);
            add(new JLabel("Státusz:")); add(statuszBox);
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

