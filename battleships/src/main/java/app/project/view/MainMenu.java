package app.project.view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    public MainMenu(Runnable showCreatorWindowFunction) {
        setLayout(new GridBagLayout());
        JButton hostButton = new JButton("Załóż serwer");
        JButton clientButton = new JButton("Dołącz do gry");
        hostButton.setPreferredSize(new Dimension(200, 50));
        clientButton.setPreferredSize(new Dimension(200, 50));

        add(new JLabel("Witaj w grze w statki!", SwingConstants.CENTER));
        add(hostButton);
        add(clientButton);

        hostButton.addActionListener(e -> {
//            dispose();
//            Board board = new Board(12);
//            board.setVisible(true);
//            ServerHandler.startServer(12345);
            showCreatorWindowFunction.run();
        });

        clientButton.addActionListener(e -> {
//            dispose();
//            new ClientConfigWindow();
//            ClientHandler.connectToServer("127.0.0.1", 12345);
            showCreatorWindowFunction.run();
        });
    }

}
