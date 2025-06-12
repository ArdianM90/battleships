package app.project.view;

import app.project.controller.GameController;
import app.project.controller.networking.ClientHandler;
import app.project.controller.networking.ServerHandler;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.MAIN_MENU;

public class MainMenuPanel extends JPanel {

    private static final int PORT = 12345;
    private static final String HOST = "127.0.0.1";

    private final GameController gameController;
    private final Runnable goToShipsSetupFunction;
    private final Runnable goToGameFunction;

    public MainMenuPanel(GameController gameController,
                         Runnable goToShipsSetupFunction,
                         Runnable goToGameFunction) {
        this.gameController = gameController;
        this.goToShipsSetupFunction = goToShipsSetupFunction;
        this.goToGameFunction = goToGameFunction;
        initComponents();
    }

    private void initComponents() {
        setName(MAIN_MENU.name());
        setLayout(new GridBagLayout());

        add(new JLabel("Witaj w grze w statki!", SwingConstants.CENTER));

        JButton hostButton = new JButton("Załóż serwer");
        hostButton.setPreferredSize(new Dimension(200, 50));
        add(hostButton);

        JButton clientButton = new JButton("Dołącz do gry");
        clientButton.setPreferredSize(new Dimension(200, 50));
        add(clientButton);

        hostButton.addActionListener(_ -> {
            serverOnClickAction();
        });

        clientButton.addActionListener(_ -> {
            clientOnClickAction();
        });
    }

    private void serverOnClickAction() {
        // todo: przenieść logikę sieciową do kontrolera
        ServerHandler server = new ServerHandler(PORT, gameController::setOpponentShipsState, goToGameFunction);
        server.start();
        gameController.setNetworkHandler(server);
        System.out.println("Serwer przechodzi do edycji.");
        goToShipsSetupFunction.run();
    }

    private void clientOnClickAction() {
        // todo: przenieść logikę sieciową do kontrolera
        ClientHandler client = new ClientHandler(HOST, PORT, gameController::setOpponentShipsState, goToGameFunction);
        client.start();
        gameController.setNetworkHandler(client);
        System.out.println("Klient przechodzi do edycji.");
        goToShipsSetupFunction.run();
    }
}
