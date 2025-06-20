package app.project;

import app.project.controller.GameController;
import app.project.view.*;

import javax.swing.*;

public class Battleships extends JFrame {

    private static final int BOARD_SIZE = 12;
    private static final int SHIPS_QTY = 20;

    private final GameController gameController;
    private final AppFrame appFrame;
    private final MainMenuPanel mainMenu;
    private ShipsSetupPanel shipsSetup;
    private GameViewPanel gameView;
    private SummaryPanel summaryView;

    private final boolean[][] initialServerShipSetup = {
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

    private final boolean[][] initialClientShipSetup = {
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
        gameController = new GameController(BOARD_SIZE, SHIPS_QTY, this::goToSummaryPanel);
        appFrame = new AppFrame();
        mainMenu = new MainMenuPanel(gameController, this::goToShipsSetupPanel, this::goToGamePanel);
        shipsSetup = new ShipsSetupPanel(gameController);

        appFrame.addPanel(mainMenu);
        appFrame.setVisible(true);
    }

    private void goToShipsSetupPanel() {
        gameController.loadInitialShipsPositions(gameController.isServer() ? initialServerShipSetup : initialClientShipSetup);
        shipsSetup = new ShipsSetupPanel(gameController);
        appFrame.addPanel(shipsSetup);
        appFrame.switchToShipsSetupPanel();
    }

    private void goToGamePanel() {
        gameView = new GameViewPanel(gameController);
        appFrame.addPanel(gameView);
        appFrame.switchToGamePanel();
    }

    private void goToSummaryPanel() {
        summaryView = new SummaryPanel(gameController.getBoardSize(), gameController.getStats());
        appFrame.addPanel(summaryView);
        appFrame.switchToSummaryPanel();
    }
}
