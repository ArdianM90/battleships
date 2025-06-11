package app.project.controller.local;

import app.project.model.BoardModel;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.BiPredicate;


public class GameEngine {

    private final int boardSize;
    private BoardModel myShips;
    private BoardModel foeShips;

    private boolean[][] tmpShipSetup = {
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, true, true, true, true, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, true, true, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, true, false, false, true, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, true, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false, false, false, true, false, false},
            {false, true, false, false, false, false, true, false, false, true, false, false},
            {false, true, false, true, false, false, true, false, false, true, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false}
    };
    private boolean[][] tmpOponnentShipSetup = {
            {false, false, false, false, true,  true,  true,  true,  false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {true,  false, false, false, false, false, false, true, true,  true,  false, false},
            {true,  false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, true },
            {false, false, false, false, false, true, false, false, true, false, false, true },
            {false, true,  true,  false, false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false, false, false, false, false},
            {false, false, false, false, true,  false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, false, false, false, true, false},
            {false, false, false, false, false, false, false, true,  false, false, false, false}
    };

    public GameEngine(int boardSize) {
        this.boardSize = boardSize;
        myShips = new BoardModel(boardSize);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tmpShipSetup[row][col]) {
                    myShips.toggleIsShip(row, col);
                }
            }
        }
        foeShips = new BoardModel(boardSize);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tmpOponnentShipSetup[row][col]) {
                    foeShips.toggleIsShip(row, col);
                }
            }
        }
    }

    public void setShipAt(Point point) {
        myShips.toggleIsShip(point.x, point.y);
    }

    public boolean saveShotAt(boolean foeBoard, Point point) {
        return foeBoard ? foeShips.shotAt(point.x, point.y) : myShips.shotAt(point.x, point.y);
    }

    public BiPredicate<Point, BoardType> isShipFunction() {
        return (point, boardType) -> boardType.equals(BoardType.FOE_BOARD)
                ? foeShips.getIsShip(point.x, point.y)
                : myShips.getIsShip(point.x, point.y);
    }

    public int getBoardSize() {
        return boardSize;
    }
}
