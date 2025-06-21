package app.project.view;

import app.project.model.BoardTileModel;
import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.GameSettings.BOARD_SIZE;

public class BoardView extends JPanel {

    private static final int BUTTON_SIZE_MID = 35;
    private static final int BUTTON_SIZE_SMALL = 20;

    private final boolean showEnemyShips;
    private final BoardType boardType;
    private final ShipTileView[][] shipsArr;
    private final Predicate<Point> isShipFunction;
    private final BiConsumer<BoardType, Point> notifyClickFunction;

    public BoardView(boolean showEnemyShips,
                     BoardType boardType,
                     Predicate<Point> isShipFunction,
                     BiConsumer<BoardType, Point> notifyClickFunction) {
        this.showEnemyShips = showEnemyShips;
        this.isShipFunction = isShipFunction;
        this.notifyClickFunction = notifyClickFunction;
        this.boardType = boardType;
        this.shipsArr = new ShipTileView[BOARD_SIZE][BOARD_SIZE];
        System.out.println("Pierwszy konstruktor BoardView");
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE_MID + 1) * BOARD_SIZE, (BUTTON_SIZE_MID + 1) * BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int finalRow = row;
                int finalCol = col;
                Runnable clickFunction = () -> this.notifyClickFunction.accept(this.boardType, new Point(finalRow, finalCol));
                this.shipsArr[row][col] = new ShipTileView(BUTTON_SIZE_MID, getDrawTileAsShipFunction(row, col), clickFunction);
                add(this.shipsArr[row][col]);
            }
        }
    }

    public BoardView(BoardType boardType, BoardTileModel[][] boardState) {
        this.showEnemyShips = false;
        this.notifyClickFunction = null;
        this.boardType = boardType;
        this.shipsArr = new ShipTileView[BOARD_SIZE][BOARD_SIZE];
        this.isShipFunction = (point) -> (boardState[point.x][point.y].isShip());
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE_SMALL + 1) * BOARD_SIZE, (BUTTON_SIZE_SMALL + 1) * BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
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

    private Supplier<Boolean> getDrawTileAsShipFunction(int row, int col) {
        return () -> {
            if (!FOE_BOARD.equals(this.boardType) || showEnemyShips) {
                return this.isShipFunction.test(new Point(row, col));
            }
            return false;
        };
    }
}