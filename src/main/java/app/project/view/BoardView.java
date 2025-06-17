package app.project.view;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.SETUP_BOARD;

public class BoardView extends JPanel {

    private static final int BUTTON_SIZE = 35;

    private final BoardType boardType;
    private final ShipTile[][] shipsArr;
    private final BiConsumer<BoardType, Point> notifyClickFunction;
    private final BiPredicate<BoardType, Point> isShipFunction;

    public BoardView(BoardType boardType, int size,
                     BiConsumer<BoardType, Point> notifyClickFunction,
                     BiPredicate<BoardType, Point> isShipFunction) {
        this.notifyClickFunction = notifyClickFunction;
        this.isShipFunction = isShipFunction;
        this.boardType = boardType;
        this.shipsArr = new ShipTile[size][size];
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * size, (BUTTON_SIZE + 1) * size));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.shipsArr[row][col] = createTile(new Point(row, col));
                add(this.shipsArr[row][col]);
            }
        }
    }

    private ShipTile createTile(Point point) {
        ShipTile tile = new ShipTile();
        tile.setBackground(isShipFunction.test(boardType, point) ? Color.RED : Color.BLUE);
        tile.setOpaque(true);
        tile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        tile.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SETUP_BOARD.equals(boardType) || FOE_BOARD.equals(boardType)) {
                    notifyClickFunction.accept(boardType, point);
                }
            }
        });
        return tile;
    }

    public void toggleShip(Point point) {
        shipsArr[point.x][point.y].setShip(isShipFunction.test(boardType, point));
        shipsArr[point.x][point.y].repaint();
    }

    public void drawShot(Point point) {
        shipsArr[point.x][point.y].markShot();
        shipsArr[point.x][point.y].repaint();
    }
}