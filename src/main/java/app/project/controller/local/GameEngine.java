package app.project.controller.local;

import app.project.model.BoardTileModel;
import app.project.model.BoardModel;
import app.project.model.BoardType;
import app.project.model.GameSettings;

import java.awt.*;

import static app.project.model.BoardType.*;

public class GameEngine {

    private final BoardModel myShips;
    private final BoardModel foeShips;

    private boolean isMyTurn;
    private String playerName;
    private String opponentName;

    public GameEngine() {
        this.isMyTurn = false;
        this.myShips = new BoardModel();
        this.foeShips = new BoardModel();
    }

    public void toggleMyShipAt(Point point) {
        myShips.toggleIsShip(point.x, point.y);
    }

    public void saveOpponentShips(Boolean[][] shipsState) {
        for (int row = 0; row < GameSettings.BOARD_SIZE; row++) {
            for (int col = 0; col < GameSettings.BOARD_SIZE; col++) {
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
            isMyTurn = !isMyTurn;
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

    public int countShips(BoardType boardType) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.countShips();
            case PLAYER_BOARD, SETUP_BOARD -> myShips.countShips();
        };
    }

    public int countSunkenShips(BoardType boardType) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.countHitShips();
            case PLAYER_BOARD -> myShips.countHitShips();
            default -> throw new IllegalArgumentException("Błąd podczas liczenia zatopień - niepoprawny typ planszy: " + boardType);
        };
    }

    public boolean endConditionsMet() {
        return GameSettings.SHIPS_QTY == countSunkenShips(PLAYER_BOARD) || GameSettings.SHIPS_QTY == countSunkenShips(FOE_BOARD);
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public boolean isShot(BoardType boardType, Point point) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.isShot(point.x, point.y);
            case PLAYER_BOARD -> myShips.isShot(point.x, point.y);
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
    }

    public void setMyTurn(boolean myTurn) {
        this.isMyTurn = myTurn;
    }

    public BoardTileModel[][] getBoardState(BoardType boardType) {
        return switch (boardType) {
            case FOE_BOARD -> foeShips.getCells();
            case PLAYER_BOARD -> myShips.getCells();
            default -> null;
        };
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
}
