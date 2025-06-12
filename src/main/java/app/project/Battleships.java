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
    private final ShipsSetupPanel shipsSetup;
    private GameViewPanel gameView;

    public Battleships() {
        gameController = new GameController(BOARD_SIZE);
        overlayFrame = new OverlayFrame();
        mainMenu = new MainMenuPanel(gameController, overlayFrame::switchToShipsSetupPanel, this::goToGameWindow);
        shipsSetup = new ShipsSetupPanel(gameController);

        overlayFrame.addPanel(mainMenu);
        overlayFrame.addPanel(shipsSetup);
        overlayFrame.setVisible(true);
    }

    private void goToGameWindow() {
        addGameView();
        overlayFrame.switchToGamePanel();
    }

    private void addGameView() {
        gameView = new GameViewPanel(gameController);
        overlayFrame.addPanel(gameView);
    }
}
