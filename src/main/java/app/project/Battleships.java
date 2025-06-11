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

    private final Runnable goToSetupFunction;
    private final Runnable goToGameFunction;

    public Battleships() {
        this.gameController = new GameController(BOARD_SIZE);
        this.overlayFrame = new OverlayFrame();
        this.goToSetupFunction = overlayFrame.switchToShipsSetupPanelFunction();
        this.goToGameFunction = overlayFrame.switchToGamePanelFunction();

        MainMenuPanel mainMenu = new MainMenuPanel(gameController::setNetworkHandler, this.goToSetupFunction, this.goToGameFunction);
        ShipsSetupPanel shipsSetup = new ShipsSetupPanel(gameController);
        GameViewPanel gameView = new GameViewPanel(gameController);

        overlayFrame.addPanel(mainMenu);
        overlayFrame.addPanel(shipsSetup);
        overlayFrame.addPanel(gameView);
        overlayFrame.setVisible(true);
    }
}
