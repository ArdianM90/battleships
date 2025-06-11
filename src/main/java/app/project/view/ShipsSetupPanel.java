package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static app.project.model.AppStage.SHIPS_SETUP;
import static app.project.model.BoardType.SETUP_BOARD;

public class ShipsSetupPanel extends JPanel {
    private Board board;

    public ShipsSetupPanel(GameController gameController) {
        gameController.setHandleMarkShipFunction(markShipOnBoardFunction());

        setName(SHIPS_SETUP.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JTextField nickInput = new JTextField("Twój nick");
        nickInput.setPreferredSize(new Dimension(800, 40));
        nickInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(nickInput);
        JLabel networkMsgLabel = new JLabel();
        networkMsgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        networkMsgLabel.setText("Wiadomość sieciowa:");
        topPanel.add(networkMsgLabel);
        add(topPanel, BorderLayout.NORTH);

        this.board = new Board(SETUP_BOARD, gameController.getBoardSize(),
                gameController.getHandleBoardClickFunction(),
                gameController.isShipFunction());
        JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        JButton readyButton = new JButton("Gotowe");
        readyButton.setPreferredSize(new Dimension(800, 40));
        readyButton.addActionListener(e -> {
            gameController.notifySetupReadiness();
        });
        add(readyButton, BorderLayout.SOUTH);
    }

    public Consumer<Point> markShipOnBoardFunction() {
        return (point) -> {
            board.markShip(point);
        };
    }
}
