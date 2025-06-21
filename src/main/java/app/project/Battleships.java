package app.project;

import app.project.controller.GameController;
import app.project.model.GameSettings;
import app.project.view.*;

import javax.swing.*;

public class Battleships extends JFrame {

    private final GameSettings settings;
    private final GameController gameController;
    private final AppFrame appFrame;
    private final MainMenuPanel mainMenu;
    private ShipsSetupPanel shipsSetup;
    private GameViewPanel gameView;
    private SummaryPanel summaryView;

    public Battleships(boolean testMode) {
        this.settings = new GameSettings(testMode);
        this.gameController = new GameController(settings, this::goToSummaryPanel);
        this.appFrame = new AppFrame();
        this.mainMenu = new MainMenuPanel(settings, gameController, this::goToShipsSetupPanel, this::goToGamePanel);

        this.appFrame.addPanel(mainMenu);
        this.appFrame.setVisible(true);
    }

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

    private void goToShipsSetupPanel() {
        shipsSetup = new ShipsSetupPanel(settings, gameController);
        if (settings.isLoadInitialShipsSetup()) {
            gameController.loadInitialShipsPositions(gameController.isServer() ? initialServerShipSetup : initialClientShipSetup);
        }
        appFrame.addPanel(shipsSetup);
        appFrame.switchToShipsSetupPanel();
    }

    private void goToGamePanel() {
        gameView = new GameViewPanel(settings, gameController);
        appFrame.addPanel(gameView);
        appFrame.switchToGamePanel();
    }

    private void goToSummaryPanel() {
        summaryView = new SummaryPanel(gameController.getStats());
        appFrame.addPanel(summaryView);
        appFrame.switchToSummaryPanel();
    }
}
