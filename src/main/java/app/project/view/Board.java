package app.project.view;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static app.project.model.BoardType.FOE_BOARD;

public class Board extends JPanel {

    private static final int BUTTON_SIZE = 35;

    private final boolean isFoeBoard;
    private final JButton[][] rectsArr;
    private final BiPredicate<Point, Boolean> isShipFunction;
    private final BiConsumer<Point, Boolean> toggleShipFunction;

    public Board(BoardType boardType, int size, BiPredicate<Point, Boolean> isShipFunction, BiConsumer<Point, Boolean> toggleShipFunction) {
        this.isShipFunction = isShipFunction;
        this.toggleShipFunction = toggleShipFunction;
        this.isFoeBoard = boardType.equals(FOE_BOARD);

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

    public Consumer<Point> markShotFunction() {
        return (point) -> {
            JButton button = rectsArr[point.x][point.y];
            button.setText("X");
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
        };
    }

    private JButton createButton(Point point) {
        JButton button = new JButton();
        button.setBackground(isShipFunction.test(point, isFoeBoard) ? Color.RED : Color.BLUE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));

        button.addActionListener(e -> {
            toggleShipFunction.accept(point, isFoeBoard);
            button.setBackground(isShipFunction.test(point, isFoeBoard) ? Color.RED : Color.BLUE);
            System.out.println("Kliknięto pole: " + point.x + ", " + point.y);
        });
        return button;
    }
}