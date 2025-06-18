package app.project.view;

import app.project.controller.GameController;
import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

import static app.project.model.AppStage.GAME;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameViewPanel extends JPanel {

    private final Function<BoardType, Integer> countHitsQtyFunction;
    private final BoardView myBoardView;
    private final BoardView foeBoardView;
    private final JLabel myBoardLabel;
    private final JLabel foeBoardLabel;
    private final int shipsPerBoardQty;

    private JLabel turnLabel;

    public GameViewPanel(GameController gameController) {
        this.countHitsQtyFunction = gameController.getCountSunkenShipsFunction();
        this.myBoardView = new BoardView(PLAYER_BOARD, gameController.getBoardSize(), gameController.getIsShipFunction(PLAYER_BOARD), gameController::handleBoardClick);
        this.foeBoardView = new BoardView(FOE_BOARD, gameController.getBoardSize(), gameController.getIsShipFunction(FOE_BOARD), gameController::handleBoardClick);
        this.myBoardLabel = new JLabel("", SwingConstants.CENTER);
        this.foeBoardLabel = new JLabel("", SwingConstants.CENTER);
        this.shipsPerBoardQty = gameController.getShipsPerBoardQty();
        gameController.setDrawShotCallback(this::drawShotOnBoard);
        gameController.setTurnLabelCallback(this::switchTurnLabel);
        initComponents(gameController.isServer());
    }

    private void initComponents(boolean isServer) {
        setName(GAME.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel topPanel = new JPanel(new BorderLayout());
        this.turnLabel = new JLabel(isServer ? "TWOJA TURA" : "PRZECIWNIK ZACZYNA", SwingConstants.CENTER);
        turnLabel.setForeground(isServer ? Color.GREEN.darker() : Color.RED);
        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(turnLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(boardWithLabel(myBoardLabel, myBoardView));
        boardsPanel.add(boardWithLabel(foeBoardLabel, foeBoardView));
        add(boardsPanel, BorderLayout.CENTER);
        updateBoardLabel(PLAYER_BOARD, 0, shipsPerBoardQty);
        updateBoardLabel(FOE_BOARD, 0, shipsPerBoardQty);
    }

    private JPanel boardWithLabel(JLabel label, BoardView board) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(board);
        return panel;
    }

    public void switchTurnLabel(boolean myTurn) {
        turnLabel.setText(myTurn ? "TWOJA TURA" : "TURA PRZECIWNIKA");
        turnLabel.setForeground(myTurn ? Color.GREEN.darker() : Color.RED);
    }

    public void updateBoardLabel(BoardType boardType, int hitsQty, int shipsGty) {
        switch (boardType) {
            case FOE_BOARD -> foeBoardLabel.setText("Plansza przeciwnika: " + hitsQty + "/" + shipsGty + " trafionych");
            case PLAYER_BOARD -> myBoardLabel.setText("Twoja plansza: " + hitsQty + "/" + shipsGty + " trafionych");
            default -> throw new IllegalArgumentException(
                    "Błąd podczas generowania nagłówka planszy - niepoprawny typ planszy: " + boardType
            );
        }
    }

    private void drawShotOnBoard(BoardType boardType, Point point) {
        switch (boardType) {
            case FOE_BOARD -> {
                foeBoardView.drawShot(point);
                updateBoardLabel(FOE_BOARD, countHitsQtyFunction.apply(boardType), shipsPerBoardQty);
            }
            case PLAYER_BOARD -> {
                myBoardView.drawShot(point);
                updateBoardLabel(PLAYER_BOARD, countHitsQtyFunction.apply(boardType), shipsPerBoardQty);
            }
            default -> throw new IllegalArgumentException("Nieznany typ planszy: " + boardType);
        }
    }
}
