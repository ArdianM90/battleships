package app.project.view;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static app.project.model.BoardType.OPPONENT_BOARD;

public class Board extends JPanel {

    private static final int BUTTON_SIZE = 35;

    private final boolean isOpponentBoard;
    BiPredicate<Point, Boolean> isShipFunction;
    BiConsumer<Point, Boolean> toggleShipFunction;

    public Board(BoardType boardType, int size, BiPredicate<Point, Boolean> isShipFunction, BiConsumer<Point, Boolean> toggleShipFunction) {
        this.isShipFunction = isShipFunction;
        this.toggleShipFunction = toggleShipFunction;
        this.isOpponentBoard = boardType.equals(OPPONENT_BOARD);

        JButton[][] rectsArr = new JButton[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * size, (BUTTON_SIZE + 1) * size));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JButton button = createButton(row, col);
                rectsArr[row][col] = button;
                add(button);
            }
        }
    }

    private JButton createButton(int row, int col) {
        JButton button = new JButton();
        button.setBackground(isShipFunction.test(new Point(row, col), isOpponentBoard) ? Color.RED : Color.BLUE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        button.addActionListener(e -> {
            toggleShipFunction.accept(new Point(row, col), isOpponentBoard);
            button.setBackground(isShipFunction.test(new Point(row, col), isOpponentBoard) ? Color.RED : Color.BLUE);
            System.out.println("Kliknięto pole: " + row + ", " + col);
        });
        return button;
    }
}