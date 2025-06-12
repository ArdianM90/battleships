package app.project.view;

import app.project.controller.GameController;
import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.GAME;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameViewPanel extends JPanel {

    private final BoardView myBoardView;
    private final BoardView foeBoardView;

    public GameViewPanel(GameController gameController) {
        this.myBoardView = new BoardView(PLAYER_BOARD, gameController.getBoardSize(), gameController::handleBoardClick, gameController.getIsShipFunction());
        this.foeBoardView = new BoardView(FOE_BOARD, gameController.getBoardSize(), gameController::handleBoardClick, gameController.getIsShipFunction());
        gameController.setDrawShotCallback(this::drawShotOnBoard);
        initComponents();
    }

    private void initComponents() {
        setName(GAME.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(new JLabel("Twoja plansza"));
        labelsPanel.add(new JLabel("Plansza przeciwnika"));
        add(labelsPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(myBoardView);
        boardsPanel.add(foeBoardView);
        add(boardsPanel, BorderLayout.CENTER);
    }

    private void drawShotOnBoard(BoardType boardType, Point point) {
        switch (boardType) {
            case FOE_BOARD:
                foeBoardView.drawShot(point);
                break;
            case PLAYER_BOARD:
                myBoardView.drawShot(point);
                break;
        }
    }
}
