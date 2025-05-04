package view;


import javax.swing.*;

/**
 * A GUI fo ablaka
 */
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
