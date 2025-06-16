package app.project.controller.local;

import app.project.model.BoardModel;
import app.project.model.BoardType;

import java.awt.*;

import static app.project.model.BoardType.*;


public class GameEngine {

    private final int boardSize;
    private final int shipsQty;
    private final BoardModel myShips;
    private final BoardModel foeShips;

    public GameEngine(int boardSize, int shipsQty) {
        this.boardSize = boardSize;
        this.shipsQty = shipsQty;
        myShips = new BoardModel(boardSize);
        foeShips = new BoardModel(boardSize);
    }

    public void toggleMyShipAt(Point point) {
        myShips.setIsShip(point.x, point.y, !myShips.getIsShip(point.x, point.y));
        myShips.print(true);
    }

    public void saveOpponentShips(Boolean[][] shipsState) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                foeShips.setIsShip(row, col, shipsState[row][col]);
            }
        }
    }

    public boolean saveShotAt(BoardType boardType, Point point) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.shotAt(point.x, point.y);
            case PLAYER_BOARD -> myShips.shotAt(point.x, point.y);
            default ->
                    throw new IllegalArgumentException("Błąd podczas obsługiwania strzału - niepoprawny typ planszy: " + boardType);
        };
    }

    public boolean isShip(BoardType boardType, Point point) {
        return FOE_BOARD.equals(boardType) ? foeShips.getIsShip(point.x, point.y) : myShips.getIsShip(point.x, point.y);
    }

    public boolean[][] getMyShipPositions() {
        return myShips.getShipPositions();
    }

    public int countSunkenShips(BoardType boardType) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.countHitShips();
            case PLAYER_BOARD -> myShips.countHitShips();
            default ->
                    throw new IllegalArgumentException("Błąd podczas liczenia zatopień - niepoprawny typ planszy: " + boardType);
        };
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getShipsQty() {
        return shipsQty;
    }
}
