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

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private GameProcessor gameProcessor;

    public AppWindow() {
        setTitle("BattleShips");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        gameProcessor = new GameProcessor(BOARD_SIZE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new MainMenu(this::showCreatorWindow), MAIN_MENU.name());
        cardPanel.add(new ShipsSetup(BOARD_SIZE, gameProcessor, this::showGameWindow), SHIPS_SETUP.name());
        cardPanel.add(new GameView(BOARD_SIZE, gameProcessor), GAME.name());

        add(cardPanel);
        setVisible(true);
    }

    private void showCreatorWindow() {
        cardLayout.show(cardPanel, SHIPS_SETUP.name());
        System.out.println("W: " + getWidth() + ", H: " + getHeight());
    }

    private void showGameWindow() {
        cardLayout.show(cardPanel, GAME.name());
        System.out.println("W: " + getWidth() + ", H: " + getHeight());
    }
}
