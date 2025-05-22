package app.project;

import app.project.controller.GameProcessor;
import app.project.view.GameView;
import app.project.view.MainMenu;
import app.project.view.ShipsSetup;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.*;

public class AppWindow extends JFrame {

    private static final int BOARD_SIZE = 12;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final GameProcessor gameProcessor;

    private SocketNetworkHandler networkHandler;

    public AppWindow() {
        setTitle("BattleShips");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        gameProcessor = new GameProcessor(BOARD_SIZE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new MainMenu(this::setNetworkHandler, this::switchToShipsSetupWindow, this::switchToGameWindow), MAIN_MENU.name());

        add(cardPanel);
        setVisible(true);
    }

    private void setNetworkHandler(SocketNetworkHandler handler) {
        this.networkHandler = handler;
    }

    private void switchToShipsSetupWindow() {
        System.out.println("Wywołuję showCreatorWindow()");

        ShipsSetup shipsSetup = new ShipsSetup(networkHandler, gameProcessor, this::switchToGameWindow);
        cardPanel.add(shipsSetup, SHIPS_SETUP.name());
        SwingUtilities.invokeLater(() -> cardLayout.show(cardPanel, SHIPS_SETUP.name()));
    }

    private void switchToGameWindow() {
        cardPanel.add(new GameView(gameProcessor), GAME.name());
        SwingUtilities.invokeLater(() -> cardLayout.show(cardPanel, GAME.name()));
        System.out.println("Wywołuję showGameWindow()");
    }

}
