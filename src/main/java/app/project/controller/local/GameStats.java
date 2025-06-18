package app.project.controller.local;

import app.project.model.BoardTileModel;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.BiPredicate;

import static app.project.model.BoardType.FOE_BOARD;

public class GameStats {
    private long gameTime;
    private int myTotalShots = 0;
    private int foeTotalShots = 0;
    private BoardTileModel[][] myBoardState = null;
    private BoardTileModel[][] foeBoardState = null;

    public void fillBoardStates(BoardTileModel[][] myBoardState, BoardTileModel[][] foeBoardState) {
        this.myBoardState = myBoardState;
        this.foeBoardState = foeBoardState;
    }

    private void startTimer() {
        gameTime = System.currentTimeMillis();
    }

    private void stopTimer() {
        long end = System.currentTimeMillis();
        gameTime = end - gameTime;
    }

    private long getGameTimeSeconds() {
        return gameTime / 1000;
    }

    public void addShot(BoardType boardType) {
        switch (boardType) {
            case PLAYER_BOARD -> myTotalShots++;
            case FOE_BOARD -> foeTotalShots++;
        }
    }

    public int getTotalShots(BoardType boardType) {
        return switch (boardType) {
            case PLAYER_BOARD -> myTotalShots;
            case FOE_BOARD -> foeTotalShots;
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
    }

    public BoardTileModel[][] getBoardState(BoardType boardType) {
        return switch (boardType) {
            case PLAYER_BOARD -> myBoardState;
            case FOE_BOARD -> foeBoardState;
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
    }

    public BiPredicate<BoardType, Point> getIsShipFunction() {
        return (boardType, point) -> {
            if (FOE_BOARD.equals(boardType)) {
                return foeBoardState[point.x][point.y].isShip();
            } else {
                return myBoardState[point.x][point.y].isShip();
            }
        };
    }
}
