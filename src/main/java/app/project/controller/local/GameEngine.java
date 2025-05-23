package app.project.controller.local;

import app.project.model.BoardModel;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;


public class GameEngine {

    private final int boardSize;
    private BoardModel myShips;
    private BoardModel foeShips;
    private Consumer<Point> myBoardShotFunction;
    private Consumer<Point> opponentBoardShotFunction;

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

    public BiPredicate<Point, Boolean> isShipFunction() {
        return (point, isOpponentBoard) -> isOpponentBoard
                ? foeShips.getIsShip(point.x, point.y)
                : myShips.getIsShip(point.x, point.y);
    }

    public BiConsumer<Point, Boolean> toggleShipFunction() {
        return (point, isOpponentBoard) -> {
            if (isOpponentBoard) {
                foeShips.toggleIsShip(point.x, point.y);
            } else {
                myShips.toggleIsShip(point.x, point.y);
            }
        };
    }

    public BiConsumer<Point, Boolean> markShotFunction() {
        return (point, byOpponent) -> {
            if (byOpponent) {
                myShips.shotAt(point.x, point.y);
            } else {
                foeShips.shotAt(point.x, point.y);
                // zmiana stanu planszy przeciwnika (widok)
            }
        };
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setFoeBoardShotFunction(Consumer<Point> myBoardShotFunction) {
        this.myBoardShotFunction = myBoardShotFunction;
    }

    public void setMyBoardShotFunction(Consumer<Point> opponentBoardShotFunction) {
        this.opponentBoardShotFunction = opponentBoardShotFunction;
    }
}
