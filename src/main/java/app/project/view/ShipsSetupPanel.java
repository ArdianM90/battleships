package app.project.view;

import app.project.controller.GameController;
import app.project.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import static app.project.model.AppStage.SHIPS_SETUP;
import static app.project.model.BoardType.SETUP_BOARD;

public class ShipsSetupPanel extends JPanel {
    private final BoardView boardView;
    private final GameController gameController;
    private final JTextField nickInput;
    private final Border defaultBorder;

    public ShipsSetupPanel(GameController gameController) {
        this.boardView = new BoardView(SETUP_BOARD, gameController.getBoardSize(), gameController.getIsShipFunction(SETUP_BOARD), gameController::handleBoardClick);
        this.gameController = gameController;
        this.gameController.setShipsSetupClickCallback(this::handleSetupBoardClick);
        this.nickInput = new JTextField();
        this.defaultBorder = nickInput.getBorder();
        initComponents();
    }

    private void initComponents() {
        setName(SHIPS_SETUP.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel nickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel nickLabel = new JLabel("Twój nick:");
        nickInput.setPreferredSize(new Dimension(200, 30));
        nickInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateNameInput(nickInput.getText()); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateNameInput(nickInput.getText()); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateNameInput(nickInput.getText()); }
        });
        nickPanel.add(nickLabel);
        nickPanel.add(nickInput);
        topPanel.add(nickPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        boardPanel.add(boardView);
        JLabel infoLabel = new JLabel("Rozmieść swoje statki i naciśnij GOTOWE. Gra rozpocznie się gdy obaj gracze zgłoszą gotowość.");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(boardPanel);
        centerPanel.add(infoLabel);
        add(centerPanel, BorderLayout.CENTER);

        JButton readyButton = new JButton("Gotowe");
        readyButton.setPreferredSize(new Dimension(200, 40));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.add(readyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        readyButton.addActionListener(_ -> {
            if (!ValidationUtils.nameIsValid(nickInput.getText())) {
                nickInput.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                nickInput.setBackground(new Color(255, 228, 225));
                return;
            }
            if (gameController.isServer() && !gameController.isClientConnected()) {
                JOptionPane.showConfirmDialog(this, "Nie można jeszcze rozpocząć gry. Poczekaj aż klient połączy się z twoim serwerem.", "Klient nie jest połączony", JOptionPane.DEFAULT_OPTION);
            } else {
                gameController.setPlayerName(nickInput.getText());
                gameController.notifySetupReadiness(nickInput.getText());
                readyButton.setEnabled(false);
            }
        });
    }

    private void validateNameInput(String name) {;
        if (ValidationUtils.nameIsValid(name)) {
            nickInput.setBorder(defaultBorder);
            nickInput.setBackground(Color.WHITE);
        } else {
            nickInput.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            nickInput.setBackground(new Color(255, 228, 225));
        }
    }

    private void handleSetupBoardClick(Point point) {
        boardView.toggleShip(point);
    }
}
