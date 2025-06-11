package app.project.view;

import app.project.controller.networking.ClientHandler;
import app.project.controller.networking.ServerHandler;
import app.project.controller.networking.SocketNetworkHandler;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static app.project.model.AppStage.MAIN_MENU;

public class MainMenuPanel extends JPanel {

    private static final int PORT = 12345;
    private static final String HOST = "127.0.0.1";

    public MainMenuPanel(Consumer<SocketNetworkHandler> setNetworkHandlerFunction,
                         Runnable goToShipsSetupFunction,
                         Runnable goToGameFunction) {
        setName(MAIN_MENU.name());
        setLayout(new GridBagLayout());
        JButton hostButton = new JButton("Załóż serwer");
        JButton clientButton = new JButton("Dołącz do gry");
        hostButton.setPreferredSize(new Dimension(200, 50));
        clientButton.setPreferredSize(new Dimension(200, 50));

        add(new JLabel("Witaj w grze w statki!", SwingConstants.CENTER));
        add(hostButton);
        add(clientButton);

        hostButton.addActionListener(e -> {
            // todo: przenieść logikę sieciową do kontrolera
            ServerHandler server = new ServerHandler(PORT, goToGameFunction);
            server.start();
            setNetworkHandlerFunction.accept(server);
            System.out.println("Serwer przechodzi do edycji.");
            goToShipsSetupFunction.run();
        });

        clientButton.addActionListener(e -> {
            // todo: przenieść logikę sieciową do kontrolera
            ClientHandler client = new ClientHandler(HOST, PORT, goToGameFunction);
            client.start();
            setNetworkHandlerFunction.accept(client);
            System.out.println("Klient przechodzi do edycji.");
            goToShipsSetupFunction.run();
        });
    }
}
