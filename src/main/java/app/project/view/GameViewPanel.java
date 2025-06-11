package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

import static app.project.model.AppStage.GAME;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameViewPanel extends JPanel {

    private final Board myBoard;
    private final Board foeBoard;

    public GameViewPanel(GameController gameController) {
        gameController.setHandleMarkShotFunction(markShotOnBoardFunction());
        myBoard = new Board(PLAYER_BOARD, gameController.getBoardSize(), gameController.getHandleBoardClickFunction(), gameController.isShipFunction());
        foeBoard = new Board(FOE_BOARD, gameController.getBoardSize(), gameController.getHandleBoardClickFunction(), gameController.isShipFunction());

        setName(GAME.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(new JLabel("Twoja plansza"));
        labelsPanel.add(new JLabel("Plansza przeciwnika"));
        add(labelsPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(myBoard);
        boardsPanel.add(foeBoard);
        add(boardsPanel, BorderLayout.CENTER);
    }

    public BiConsumer<Boolean, Point> markShotOnBoardFunction() {
        return (isFoeBoard, point) -> {
            if (isFoeBoard) {
                foeBoard.markShot(point);
            } else {
                myBoard.markShot(point);
            }
        };
    }
}
