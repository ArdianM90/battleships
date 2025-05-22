package app.project.controller;

import app.project.model.BoardModel;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class GameProcessor {

    private final int boardSize;
    private BoardModel myShips;
    private BoardModel opponentShips;

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

    public GameProcessor(int boardSize) {
        this.boardSize = boardSize;
        myShips = new BoardModel(boardSize);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tmpShipSetup[row][col]) {
                    myShips.toggleIsShip(row, col);
                }
            }
        }
        opponentShips = new BoardModel(boardSize);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tmpOponnentShipSetup[row][col]) {
                    opponentShips.toggleIsShip(row, col);
                }
            }
        }
    }

    public BiPredicate<Point, Boolean> isShipFunction() {
        return (point, isOpponentBoard) -> isOpponentBoard
                ? opponentShips.getIsShip(point.x, point.y)
                : myShips.getIsShip(point.x, point.y);
    }

    public BiConsumer<Point, Boolean> toggleShipFunction() {
        return (point, isOpponentBoard) -> {
            if (isOpponentBoard) {
                opponentShips.toggleIsShip(point.x, point.y);
            } else {
                myShips.toggleIsShip(point.x, point.y);
            }
        };
    }

    public int getBoardSize() {
        return boardSize;
    }
}
