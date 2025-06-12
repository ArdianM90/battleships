package app.project.controller.local;

import app.project.model.BoardModel;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.BiPredicate;

import static app.project.model.BoardType.*;


public class GameEngine {

    private final int boardSize;
    private final BoardModel myShips;
    private final BoardModel foeShips;

    public GameEngine(int boardSize) {
        this.boardSize = boardSize;
        myShips = new BoardModel(boardSize);
        foeShips = new BoardModel(boardSize);
    }

    public void toggleMyShipAt(Point point) {
        myShips.toggleIsShip(point.x, point.y);
        myShips.print(true);
    }

    public void saveOpponentShips(Boolean[][] shipsState) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                foeShips.setIsShip(row, col, shipsState[row][col]);
            }
        }
    }

    public boolean saveShotAt(boolean onFoeBoard, Point point) {
        return onFoeBoard ? foeShips.shotAt(point.x, point.y) : myShips.shotAt(point.x, point.y);
    }

    public boolean isShip(BoardType boardType, Point point) {
        return FOE_BOARD.equals(boardType) ? foeShips.getIsShip(point.x, point.y) : myShips.getIsShip(point.x, point.y);
    }

    public boolean[][] getMyBoardState() {
        return myShips.getBoardStateArray();
    }

    public int getBoardSize() {
        return boardSize;
    }
}
