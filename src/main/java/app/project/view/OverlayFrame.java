package app.project.view;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.GAME;
import static app.project.model.AppStage.SHIPS_SETUP;

public class OverlayFrame extends JFrame {

    private final CardLayout layout;
    private final JPanel cardPanel;

    public OverlayFrame() {
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

    public Runnable switchToShipsSetupPanelFunction() {
        return () -> {
            SwingUtilities.invokeLater(() -> layout.show(cardPanel, SHIPS_SETUP.name()));
        };
    }

    public Runnable switchToGamePanelFunction() {
        return () -> {
            SwingUtilities.invokeLater(() -> layout.show(cardPanel, GAME.name()));
        };
    }
}
