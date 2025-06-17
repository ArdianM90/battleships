package app.project.view;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.SETUP_BOARD;

public class BoardView extends JPanel {

    private static final int BUTTON_SIZE = 35;

    private final BoardType boardType;
    private final JButton[][] rectsArr;
    private final BiConsumer<BoardType, Point> notifyClickFunction;
    private final BiPredicate<BoardType, Point> isShipFunction;

    public BoardView(BoardType boardType, int size,
                     BiConsumer<BoardType, Point> notifyClickFunction,
                     BiPredicate<BoardType, Point> isShipFunction) {
        this.notifyClickFunction = notifyClickFunction;
        this.isShipFunction = isShipFunction;
        this.boardType = boardType;

        this.rectsArr = new JButton[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * size, (BUTTON_SIZE + 1) * size));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.rectsArr[row][col] = createButton(new Point(row, col));
                add(this.rectsArr[row][col]);
            }
        }
    }

    private JButton createButton(Point point) {
        // todo: przenieść sterowanie kolorem do kontrolera
        JButton button = new JButton();
        button.setBackground(isShipFunction.test(boardType, point) ? Color.RED : Color.BLUE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        button.addActionListener(_ -> shipOnClick(point));
        return button;
    }

    private void shipOnClick(Point point) {
        if (SETUP_BOARD.equals(boardType)) {
            notifyClickFunction.accept(boardType, point);
        } else if (BoardType.FOE_BOARD.equals(boardType)) {
            notifyClickFunction.accept(boardType, point);
        }
    }

    public void markShip(Point point) {
        JButton button = rectsArr[point.x][point.y];
        Color newColor = isShipFunction.test(boardType, point) ? Color.RED : Color.BLUE;
        button.setBackground(newColor);
    }

    public void drawShot(Point point) {
        JButton button = rectsArr[point.x][point.y];
        button.setText("X");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
    }
}