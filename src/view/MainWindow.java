package view;

import javax.swing.*;
import java.awt.*;

/**
 * A GUI fo ablaka
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Konyvesbolt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("📚 Konyvek", new KonyvPanel());
        tabs.addTab("👤 Vasarlok", new VasarloPanel());
        tabs.addTab("🛒 Rendelések", new RendelesPanel());

        // Exit gomb
        JButton exitButton = new JButton("🚪 Kilepes");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Biztosan ki akarsz lepni?",
                    "Kilepes megerositese",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(exitButton);

        add(tabs, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}