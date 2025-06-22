package app.project;

import app.project.controller.GameController;
import app.project.model.GameSettings;
import app.project.utils.FilesUtils;
import app.project.view.*;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

import static app.project.model.GameSettings.BOARD_SIZE;

public class Battleships extends JFrame {

    private static final String SERVER_IDENT = "server";
    private static final String CLIENT_IDENT = "client";

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

    private void goToShipsSetupPanel() {
        shipsSetup = new ShipsSetupPanel(settings, gameController);
        if (settings.isLoadInitialShipsSetup()) {
            boolean[][] positions = FilesUtils.loadShipPositions(gameController.isServer() ? SERVER_IDENT : CLIENT_IDENT);
            gameController.loadInitialShipsPositions(positions);
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
        FilesUtils.saveGameStatsFile(gameController.getStats());
        summaryView = new SummaryPanel(gameController.getStats());
        appFrame.addPanel(summaryView);
        appFrame.switchToSummaryPanel();
    }
}
