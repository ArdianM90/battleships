package app.project.view.board;

import app.project.model.BoardTileModel;
import app.project.model.types.BoardType;

import java.awt.*;
import java.util.function.Supplier;

import static app.project.model.GameSettings.BOARD_SIZE;

public class StaticBoard extends BoardView {

    private static final int BUTTON_SIZE = 20;

    public StaticBoard(BoardType boardType, BoardTileModel[][] boardState) {
        this.showEnemyShips = false;
        this.notifyClickFunction = null;
        this.boardType = boardType;
        this.shipsArr = new ShipTileView[BOARD_SIZE][BOARD_SIZE];
        this.isShipFunction = (point) -> (boardState[point.x][point.y].isShip());
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 1, 1));
        setPreferredSize(new Dimension((BUTTON_SIZE + 1) * BOARD_SIZE, (BUTTON_SIZE + 1) * BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int finalRow = row;
                int finalCol = col;
                Supplier<Boolean> changeColorFunction = () -> this.isShipFunction.test(new Point(finalRow, finalCol));
                this.shipsArr[row][col] = new ShipTileView(BUTTON_SIZE, changeColorFunction, null);
                if (boardState[row][col].isHit()) {
                    this.shipsArr[row][col].drawShot();
                }
                add(this.shipsArr[row][col]);
            }
        }
    }
}
