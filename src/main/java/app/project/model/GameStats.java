package app.project.model;

import app.project.model.types.BoardType;

import java.util.Arrays;

import static app.project.model.types.BoardType.FOE_BOARD;

public class GameStats {
    private long gameTime;
    private String playerName;
    private String opponentName;
    private BoardTileModel[][] myBoardState = null;
    private BoardTileModel[][] foeBoardState = null;

    public GameStats(String playerName, String opponentName) {
        this.gameTime = System.currentTimeMillis();
        this.playerName = playerName;
        this.opponentName = opponentName;
    }

    public void saveFinalState(BoardTileModel[][] myBoardState, BoardTileModel[][] foeBoardState) {
        this.myBoardState = myBoardState;
        this.foeBoardState = foeBoardState;
        stopTimer();
    }

    private void stopTimer() {
        long end = System.currentTimeMillis();
        gameTime = end - gameTime;
    }

    public long getGameTimeSeconds() {
        return gameTime / 1000;
    }

    public int getTotalShots(BoardType boardType) {
        return switch (boardType) {
            case PLAYER_BOARD -> Arrays.stream(myBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(tile -> tile.isHit() ? 1 : 0)
                    .sum();
            case FOE_BOARD -> Arrays.stream(foeBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(tile -> tile.isHit() ? 1 : 0)
                    .sum();
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
    }

    public int getAccurateShots(BoardType boardType) {
        return switch (boardType) {
            case PLAYER_BOARD -> Arrays.stream(myBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(tile -> tile.isHit() && tile.isShip() ? 1 : 0)
                    .sum();
            case FOE_BOARD -> Arrays.stream(foeBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(tile -> tile.isHit() && tile.isShip() ? 1 : 0)
                    .sum();
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

    public int countFloatingShips(BoardType boardType) {
        return switch (boardType) {
            case PLAYER_BOARD -> Arrays.stream(myBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(e -> !e.isHit() && e.isShip() ? 1 : 0)
                    .sum();
            case FOE_BOARD -> Arrays.stream(foeBoardState)
                    .flatMap(Arrays::stream)
                    .mapToInt(e -> !e.isHit() && e.isShip() ? 1 : 0)
                    .sum();
            default -> throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        };
    }

    public boolean isWinner() {
        return countFloatingShips(FOE_BOARD) == 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }
}
