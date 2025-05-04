import view.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Always use SwingUtilities.invokeLater for GUI startup
        SwingUtilities.invokeLater(MainWindow::new);
    }
}