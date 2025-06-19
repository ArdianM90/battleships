package app.project.controller.local;

import app.project.model.BoardTileModel;
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

    public void shotAt(BoardType targetBoard, Point point) {
        myShips.print(true);
        foeShips.print(false);
        boolean shipHit = switch (targetBoard) {
            case PLAYER_BOARD -> myShips.shot(point.x, point.y);
            case FOE_BOARD -> foeShips.shot(point.x, point.y);
            default -> throw new IllegalArgumentException("Błąd podczas obsługiwania strzału - niepoprawny typ planszy: " + targetBoard);
        };
        if (!shipHit) {
            myTurn = !myTurn;
        }
    }

    public boolean isFoeShip(Point point) {
        return foeShips.isShip(point.x, point.y);
    }

    public boolean isMyShip(Point point) {
        return myShips.isShip(point.x, point.y);
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

    public boolean isShot(BoardType boardType, Point point) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.isShot(point.x, point.y);
            case PLAYER_BOARD -> myShips.isShot(point.x, point.y);
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
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

    public BoardTileModel[][] getBoardState(BoardType boardType) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.getCells();
            case PLAYER_BOARD -> myShips.getCells();
            default -> null;
        };
    }
}
