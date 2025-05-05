package view;

import javax.swing.*;

/**
 * A GUI fo ablaka
 */
// TODO: torolheto, ha uj megoldas tesztelt.
/*
public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Konyvesbolt alkalmazas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Add the KonyvPanel to the frame
        add(new KonyvPanel());

        setVisible(true);
    }
}
*/
public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Könyvesbolt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("📚 Konyvek", new KonyvPanel());
        tabs.addTab("👤 Vasarlok", new VasarloPanel());

        add(tabs);
        setVisible(true);
    }
}