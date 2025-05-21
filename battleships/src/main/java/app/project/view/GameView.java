package app.project.view;

import app.project.controller.GameProcessor;

import javax.swing.*;
import java.awt.*;

import static app.project.model.BoardType.OPPONENT_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameView extends JPanel {

    private Board myBoard;
    private Board opponentBoard;

    public GameView(int boardSize, GameProcessor gameProcessor) {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        myBoard = new Board(boardSize, PLAYER_BOARD, gameProcessor.isShipFunction(), gameProcessor.toggleShipFunction());
        opponentBoard = new Board(boardSize, OPPONENT_BOARD, gameProcessor.isShipFunction(), gameProcessor.toggleShipFunction());

        JPanel labelsPanel = new JPanel(new GridLayout(1, 2));
        labelsPanel.add(new JLabel("Twoja plansza"));
        labelsPanel.add(new JLabel("Plansza przeciwnika"));
        add(labelsPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(myBoard);
        boardsPanel.add(opponentBoard);
        add(boardsPanel, BorderLayout.CENTER);
    }
}
