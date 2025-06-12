package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.GAME;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameViewPanel extends JPanel {

    private final BoardView myBoardView;
    private final BoardView foeBoardView;

    public GameViewPanel(GameController gameController) {
        this.myBoardView = new BoardView(PLAYER_BOARD, gameController.getBoardSize(), gameController::handleBoardClick, gameController.isShipFunction());
        this.foeBoardView = new BoardView(FOE_BOARD, gameController.getBoardSize(), gameController::handleBoardClick, gameController.isShipFunction());
        gameController.setHandleMarkShotFunction(this::markShotOnBoard);
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

    private void markShotOnBoard(boolean foeBoard, Point point) {
        if (foeBoard) {
            foeBoardView.markShot(point);
        } else {
            myBoardView.markShot(point);
        }
    }
}
