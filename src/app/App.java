package app;

import controllers.MapController;
import views.MainFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            new MapController(frame);
            frame.setVisible(true);
        });
    }
}
