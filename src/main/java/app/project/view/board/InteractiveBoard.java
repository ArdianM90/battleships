package app.project.view.board;

import app.project.model.types.BoardType;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static app.project.model.GameSettings.BOARD_SIZE;

public class InteractiveBoard extends BoardView {

    private static final int BUTTON_SIZE = 35;

    public InteractiveBoard(boolean showEnemyShips,
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
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * BOARD_SIZE, (BUTTON_SIZE + 1) * BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int finalRow = row;
                int finalCol = col;
                Runnable clickFunction = () -> this.notifyClickFunction.accept(this.boardType, new Point(finalRow, finalCol));
                this.shipsArr[row][col] = new ShipTileView(BUTTON_SIZE, getDrawTileAsShipFunction(row, col), clickFunction);
                add(this.shipsArr[row][col]);
            }
        }
    }
}
