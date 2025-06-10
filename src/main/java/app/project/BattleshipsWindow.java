package app.project;

import app.project.controller.GameController;
import app.project.view.GameView;
import app.project.view.MainMenu;
import app.project.view.ShipsSetup;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.*;

public class BattleshipsWindow extends JFrame {

    private static final int BOARD_SIZE = 12;

    private final GameController gameController;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public BattleshipsWindow() {
        setTitle("BattleShips");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        gameController = new GameController(BOARD_SIZE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        MainMenu mainMenu = new MainMenu(gameController::setNetworkHandler, this::switchToShipsSetupWindow, this::switchToGameWindow);
        cardPanel.add(mainMenu, MAIN_MENU.name());

        add(cardPanel);
        setVisible(true);
    }

    private void switchToShipsSetupWindow() {
        ShipsSetup shipsSetup = new ShipsSetup(gameController);
        cardPanel.add(shipsSetup, SHIPS_SETUP.name());
        SwingUtilities.invokeLater(() -> cardLayout.show(cardPanel, SHIPS_SETUP.name()));
    }

    private void switchToGameWindow() {
        GameView gameView = new GameView(gameController);
        cardPanel.add(gameView, GAME.name());
        SwingUtilities.invokeLater(() -> cardLayout.show(cardPanel, GAME.name()));
    }
}
