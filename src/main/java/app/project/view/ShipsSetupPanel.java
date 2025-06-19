package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.SHIPS_SETUP;
import static app.project.model.BoardType.SETUP_BOARD;

public class ShipsSetupPanel extends JPanel {
    private final BoardView boardView;
    private final GameController gameController;

    public ShipsSetupPanel(GameController gameController) {
        this.boardView = new BoardView(SETUP_BOARD, gameController.getBoardSize(), gameController.getIsShipFunction(SETUP_BOARD), gameController::handleBoardClick);
        this.gameController = gameController;
        this.gameController.setShipsSetupClickCallback(this::handleSetupBoardClick);
        initComponents();
    }

    private void initComponents() {
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

        JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        boardPanel.add(boardView);
        add(boardPanel, BorderLayout.CENTER);

        JButton readyButton = new JButton("Gotowe");
        readyButton.setPreferredSize(new Dimension(800, 40));
        add(readyButton, BorderLayout.SOUTH);

        readyButton.addActionListener(_ -> {
            if (gameController.isServer() && !gameController.isClientConnected()) {
                JOptionPane.showConfirmDialog(this, "Nie można jeszcze rozpocząć gry. Poczekaj aż klient połączy się z twoim serwerem.", "Klient nie jest połączony", JOptionPane.DEFAULT_OPTION);
            } else {
                gameController.notifySetupReadiness();
                readyButton.setEnabled(false);
            }
        });
    }

    private void handleSetupBoardClick(Point point) {
        boardView.toggleShip(point);
    }
}
