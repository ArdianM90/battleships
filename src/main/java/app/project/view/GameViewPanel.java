package app.project.view;

import app.project.controller.GameController;
import app.project.model.BoardType;
import app.project.model.GameSettings;

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
    private final String opponentName;

    private JLabel turnLabel;

    public GameViewPanel(GameSettings settings, GameController gameController) {
        this.countHitsQtyFunction = gameController.getCountSunkenShipsFunction();
        this.myBoardView = new BoardView(settings.isShowEnemyShips(), PLAYER_BOARD, gameController.getIsShipFunction(PLAYER_BOARD), gameController::handleBoardClick);
        this.foeBoardView = new BoardView(settings.isShowEnemyShips(), FOE_BOARD, gameController.getIsShipFunction(FOE_BOARD), gameController::handleBoardClick);
        this.myBoardLabel = new JLabel("", SwingConstants.CENTER);
        this.foeBoardLabel = new JLabel("", SwingConstants.CENTER);
        this.opponentName = gameController.getOpponentName();
        gameController.setDrawShotCallback(this::drawShotOnBoard);
        gameController.setTurnLabelCallback(this::switchTurnLabel);
        gameController.startTimer();
        initComponents(gameController.isMyTurn());
    }

    private void initComponents(boolean isMyTurn) {
        setName(GAME.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel topPanel = new JPanel(new BorderLayout());
        this.turnLabel = new JLabel(isMyTurn ? "TWOJA TURA" : "PRZECIWNIK ZACZYNA", SwingConstants.CENTER);
        turnLabel.setForeground(isMyTurn ? Color.GREEN.darker() : Color.RED);
        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(turnLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(boardWithLabel(myBoardLabel, myBoardView));
        boardsPanel.add(boardWithLabel(foeBoardLabel, foeBoardView));
        add(boardsPanel, BorderLayout.CENTER);
        updateBoardLabel(PLAYER_BOARD, 0);
        updateBoardLabel(FOE_BOARD, 0);
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
        turnLabel.setText(myTurn ? "TWOJA TURA" : "TURA GRACZA " + opponentName.toUpperCase());
        turnLabel.setForeground(myTurn ? Color.GREEN.darker() : Color.RED);
    }

    public void updateBoardLabel(BoardType boardType, int hitsQty) {
        switch (boardType) {
            case FOE_BOARD -> foeBoardLabel.setText("Plansza przeciwnika: " + hitsQty + "/" + GameSettings.SHIPS_QTY + " trafionych");
            case PLAYER_BOARD -> myBoardLabel.setText("Twoja plansza: " + hitsQty + "/" + GameSettings.SHIPS_QTY + " trafionych");
            default -> throw new IllegalArgumentException("Błąd podczas generowania nagłówka planszy - niepoprawny typ planszy: " + boardType);
        }
    }

    private void drawShotOnBoard(BoardType boardType, Point point) {
        switch (boardType) {
            case FOE_BOARD -> {
                foeBoardView.drawShot(point);
                updateBoardLabel(FOE_BOARD, countHitsQtyFunction.apply(boardType));
            }
            case PLAYER_BOARD -> {
                myBoardView.drawShot(point);
                updateBoardLabel(PLAYER_BOARD, countHitsQtyFunction.apply(boardType));
            }
            default -> throw new IllegalArgumentException("Nieznany typ planszy: " + boardType);
        }
    }
}
