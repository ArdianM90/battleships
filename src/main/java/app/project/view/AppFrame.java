package app.project.view;

import javax.swing.*;
import java.awt.*;

import static app.project.model.types.AppStage.*;

public class AppFrame extends JFrame {

    private final CardLayout layout;
    private final JPanel cardPanel;

    public AppFrame() {
        setTitle("BattleShips");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        layout = new CardLayout();
        cardPanel = new JPanel(layout);
        add(cardPanel);
    }

    public void addPanel(JPanel panel) {
        cardPanel.add(panel, panel.getName());
    }

    public void switchToShipsSetupPanel() {
        SwingUtilities.invokeLater(() -> layout.show(cardPanel, SHIPS_SETUP.name()));
    }

    public void switchToGamePanel() {
        SwingUtilities.invokeLater(() -> layout.show(cardPanel, GAME.name()));
    }

    public void switchToSummaryPanel() {
        JOptionPane.showConfirmDialog(this, "Gra dobiegła końca. Naciśnij OK aby przejść do podsumowania", "Koniec gry", JOptionPane.DEFAULT_OPTION);
        SwingUtilities.invokeLater(() -> layout.show(cardPanel, SUMMARY.name()));
    }
}
