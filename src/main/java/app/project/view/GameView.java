package app.project.view;

import app.project.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameView extends JPanel {

    private Board myBoard;
    private Board foeBoard;


    public GameView(GameController gameController) {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        myBoard = new Board(PLAYER_BOARD, gameController.getBoardSize(), gameController.isShipFunction(), gameController.markShotFunction());
        foeBoard = new Board(FOE_BOARD, gameController.getBoardSize(), gameController.isShipFunction(), gameController.markShotFunction());
        gameController.setMyBoardShotFunction(myBoard.markShotFunction());
        gameController.setFoeBoardShotFunction(foeBoard.markShotFunction());

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(new JLabel("Twoja plansza"));
        labelsPanel.add(new JLabel("Plansza przeciwnika"));
        add(labelsPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(myBoard);
        boardsPanel.add(foeBoard);
        add(boardsPanel, BorderLayout.CENTER);
    }

    public Consumer<Point> myBoardShotFunction() {
        return myBoard.markShotFunction();
    }

    public Consumer<Point> foeBoardShotFunction() {
        return myBoard.markShotFunction();
    }
}
