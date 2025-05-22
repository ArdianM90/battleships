package app.project.view;

import app.project.controller.GameProcessor;

import javax.swing.*;
import java.awt.*;

import static app.project.model.BoardType.SETUP_BOARD;

public class ShipsSetup extends JPanel {
    private JTextField nickInput;
    JPanel boardPanel;
    private JButton readyButton;

    public ShipsSetup(int boardSize,GameProcessor gameProcessor, Runnable showGameWindowFunction) {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        nickInput = new JTextField("Twój nick");
        nickInput.setPreferredSize(new Dimension(800, 40));
        add(nickInput, BorderLayout.NORTH);

        Board board = new Board(boardSize, SETUP_BOARD, gameProcessor.isShipFunction(), gameProcessor.toggleShipFunction());
        boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        boardPanel.add(board);
        add(boardPanel, BorderLayout.CENTER);

        readyButton = new JButton("Gotowe");
        readyButton.setPreferredSize(new Dimension(800, 40));
        readyButton.addActionListener(e -> showGameWindowFunction.run());
        add(readyButton, BorderLayout.SOUTH);
    }
}
