package view;

import javax.swing.*;

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

        add(tabs);
        setVisible(true);
    }
}