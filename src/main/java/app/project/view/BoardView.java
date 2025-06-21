package app.project.view;

import app.project.model.BoardTileModel;
import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;

public class BoardView extends JPanel {

    private static final int BUTTON_SIZE_MID = 35;
    private static final int BUTTON_SIZE_SMALL = 20;

    private final BoardType boardType;
    private final ShipTileView[][] shipsArr;
    private final Predicate<Point> isShipFunction;
    private final BiConsumer<BoardType, Point> notifyClickFunction;

    public BoardView(BoardType boardType, int size,
                     Predicate<Point> isShipFunction,
                     BiConsumer<BoardType, Point> notifyClickFunction) {
        this.isShipFunction = isShipFunction;
        this.notifyClickFunction = notifyClickFunction;
        this.boardType = boardType;
        this.shipsArr = new ShipTileView[size][size];
        System.out.println("Pierwszy konstruktor BoardView");
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE_MID + 1) * size, (BUTTON_SIZE_MID + 1) * size));
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int finalRow = row;
                int finalCol = col;
                Supplier<Boolean> drawTileAsShipFunction = () -> {
                    if (!FOE_BOARD.equals(this.boardType)) {
                        return this.isShipFunction.test(new Point(finalRow, finalCol));
                    }
                    return false;
                };
                Runnable clickFunction = () -> this.notifyClickFunction.accept(this.boardType, new Point(finalRow, finalCol));
                this.shipsArr[row][col] = new ShipTileView(BUTTON_SIZE_MID, drawTileAsShipFunction, clickFunction);
                add(this.shipsArr[row][col]);
            }
        }
    }

    public BoardView(BoardType boardType, int size, BoardTileModel[][] boardState) {
        this.notifyClickFunction = null;
        this.boardType = boardType;
        this.shipsArr = new ShipTileView[size][size];
        this.isShipFunction = (point) -> (boardState[point.x][point.y].isShip());
        setLayout(new GridLayout(size, size, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE_SMALL + 1) * size, (BUTTON_SIZE_SMALL + 1) * size));
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int finalRow = row;
                int finalCol = col;
                Supplier<Boolean> changeColorFunction = () -> this.isShipFunction.test(new Point(finalRow, finalCol));
                this.shipsArr[row][col] = new ShipTileView(BUTTON_SIZE_SMALL, changeColorFunction, null);
                if (boardState[row][col].isHit()) {
                    this.shipsArr[row][col].drawShot();
                }
                add(this.shipsArr[row][col]);
            }
        }
    }

    public void toggleShip(Point point) {
        shipsArr[point.x][point.y].setRed(isShipFunction.test(point));
    }

    public void drawShot(Point point) {
        if (FOE_BOARD.equals(boardType) && isShipFunction.test(point)) {
            toggleShip(point);
        }
        shipsArr[point.x][point.y].drawShot();
    }
}