package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameView extends JPanel {

    private Board myBoard;
    private Board foeBoard;


    public GameView(GameController gameController) {
        gameController.setMarkShotFunction(markShotOnBoardFunction());
        myBoard = new Board(PLAYER_BOARD, gameController.getBoardSize(), gameController.getNotifyClickFunction(), gameController.isShipFunction());
        foeBoard = new Board(FOE_BOARD, gameController.getBoardSize(), gameController.getNotifyClickFunction(), gameController.isShipFunction());

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
            System.out.println("Widok: mark shot");
            if (isFoeBoard) {
                foeBoard.markShot(point);
            } else {
                myBoard.markShot(point);
            }
        };
    }
}
