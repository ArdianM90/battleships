package app.project.view;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.*;

public class OverlayFrame extends JFrame {

    private static final int SUMMARY_DELAY_SECONDS = 2;

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

    public void switchToShipsSetupPanel() {
        SwingUtilities.invokeLater(() -> layout.show(cardPanel, SHIPS_SETUP.name()));
    }

    public void switchToGamePanel() {
        SwingUtilities.invokeLater(() -> layout.show(cardPanel, GAME.name()));
    }

    public void switchToSummaryPanel() {
        new Timer(SUMMARY_DELAY_SECONDS * 1000, e -> {
            SwingUtilities.invokeLater(() -> layout.show(cardPanel, SUMMARY.name()));
        }) {{
            setRepeats(false);
            start();
        }};
    }
}
