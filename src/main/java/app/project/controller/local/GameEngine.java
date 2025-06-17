package app.project.controller.local;

import app.project.model.BoardModel;
import app.project.model.BoardType;

import java.awt.*;

import static app.project.model.BoardType.*;


public class GameEngine {

    private final int boardSize;
    private final int shipsQty;
    private boolean myTurn;
    private final BoardModel myShips;
    private final BoardModel foeShips;

    public GameEngine(int boardSize, int shipsQty) {
        this.boardSize = boardSize;
        this.shipsQty = shipsQty;
        this.myTurn = false;
        this.myShips = new BoardModel(boardSize);
        this.foeShips = new BoardModel(boardSize);
    }

    public void toggleMyShipAt(Point point) {
        myShips.toggleIsShip(point.x, point.y);
        myShips.print(true);
    }

    public void saveOpponentShips(Boolean[][] shipsState) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (shipsState[row][col]) {
                    foeShips.setShip(row, col);
                }
            }
        }
    }

    public boolean saveShotAt(BoardType boardType, Point point) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.shotAt(point.x, point.y);
            case PLAYER_BOARD -> myShips.shotAt(point.x, point.y);
            default -> throw new IllegalArgumentException("Błąd podczas obsługiwania strzału - niepoprawny typ planszy: " + boardType);
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
            default -> throw new IllegalArgumentException("Błąd podczas liczenia zatopień - niepoprawny typ planszy: " + boardType);
        };
    }

    public boolean endConditionsMet() {
        return shipsQty == countSunkenShips(PLAYER_BOARD) || shipsQty == countSunkenShips(FOE_BOARD);
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getShipsQty() {
        return shipsQty;
    }
}
