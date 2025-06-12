package app.project;

import app.project.controller.GameController;
import app.project.view.GameViewPanel;
import app.project.view.MainMenuPanel;
import app.project.view.OverlayFrame;
import app.project.view.ShipsSetupPanel;

import javax.swing.*;

public class Battleships extends JFrame {

    private static final int BOARD_SIZE = 12;

    private final GameController gameController;
    private final OverlayFrame overlayFrame;
    private final MainMenuPanel mainMenu;
    private ShipsSetupPanel shipsSetup;
    private GameViewPanel gameView;

    private boolean[][] initialServerShipSetup = {
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, true, true, true, true, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, true, true, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, true, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false, false, true, false, false},
            {false, true, false, false, false, false, true, false, false, true, false, false},
            {false, true, false, true, false, false, true, false, false, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false}
    };

    private boolean[][] initialClientShipSetup = {
            {false, false, false, false, true,  true,  true,  true,  false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {true,  false, false, false, false, false, false, true, true,  true,  false, false},
            {true,  false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, true },
            {false, false, false, false, false, true, false, false, true, false, false, true },
            {false, true,  true,  false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, true,  false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, true,  false, false, false, false}
    };

    public Battleships() {
        gameController = new GameController(BOARD_SIZE);
        overlayFrame = new OverlayFrame();
        mainMenu = new MainMenuPanel(gameController, this::goToShipsSetupPanel, this::goToGamePanel);
        shipsSetup = new ShipsSetupPanel(gameController);

        overlayFrame.addPanel(mainMenu);
        overlayFrame.setVisible(true);
    }

    private void goToShipsSetupPanel() {
        shipsSetup = new ShipsSetupPanel(gameController);
        gameController.loadInitialShipsPositions(gameController.isServer() ? initialServerShipSetup : initialClientShipSetup); // for tests only
        overlayFrame.addPanel(shipsSetup);
        overlayFrame.switchToShipsSetupPanel();
    }

    private void goToGamePanel() {
        gameView = new GameViewPanel(gameController);
        overlayFrame.addPanel(gameView);
        overlayFrame.switchToGamePanel();
    }
}
