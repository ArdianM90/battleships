package app.project.view;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.SETUP_BOARD;

public class Board extends JPanel {

    private static final int BUTTON_SIZE = 35;

    private final BoardType boardType;
    private final JButton[][] rectsArr;
    private final BiConsumer<Point, BoardType> notifyClickFunction;
    private final BiPredicate<Point, BoardType> isShipFunction;

    public Board(BoardType boardType, int size,
                 BiConsumer<Point, BoardType> notifyClickFunction,
                 BiPredicate<Point, BoardType> isShipFunction) {
        this.notifyClickFunction = notifyClickFunction;
        this.isShipFunction = isShipFunction;
        this.boardType = boardType;

        rectsArr = new JButton[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * size, (BUTTON_SIZE + 1) * size));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JButton button = createButton(new Point(row, col));
                rectsArr[row][col] = button;
                add(button);
            }
        }
    }

    public void markShot(Point point) {
        JButton button = rectsArr[point.x][point.y];
        button.setText("X");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
    }

    public void markShip(Point point) {
        System.out.println("Mark ship function: " + point.x + ", " + point.y);
        JButton button = rectsArr[point.x][point.y];
        button.setBackground(isShipFunction.test(point, boardType) ? Color.RED : Color.BLUE);
    }

    private JButton createButton(Point point) {
        // todo: przenieść sterowanie kolorem do kontrolera
        JButton button = new JButton();
        button.setBackground(isShipFunction.test(point, boardType) ? Color.RED : Color.BLUE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        button.addActionListener(e -> {
            if (boardType.equals(SETUP_BOARD)) {
                markShip(point);
            } else {
                markShot(point);
            }
            notifyClickFunction.accept(point, boardType);
        });
        return button;
    }
}