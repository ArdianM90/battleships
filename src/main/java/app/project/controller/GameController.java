package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.local.GameStats;
import app.project.controller.networking.ClientHandler;
import app.project.controller.networking.NetworkUtils;
import app.project.controller.networking.ServerHandler;
import app.project.controller.networking.SocketNetworkHandler;
import app.project.model.BoardType;

import java.awt.*;
import java.util.Objects;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameController {

    private final GameEngine localEngine;
    private final Runnable gotoSummaryFunction;

    private GameStats gameStats;

    private SocketNetworkHandler networkHandler;
    private Consumer<Point> shipsSetupClickCallback;
    private BiConsumer<BoardType, Point> drawShotCallback;
    private Consumer<Boolean> turnLabelCallback;

    public GameController(int boardSize, int shipsQty, Runnable gotoSummaryFunction) {
        this.localEngine = new GameEngine(boardSize, shipsQty);
        this.gotoSummaryFunction = gotoSummaryFunction;
    }

    public void loadInitialShipsPositions(boolean[][] shipsSetup) {
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (shipsSetup[row][col]) {
                    handleSetupClick(new Point(row, col));
                }
            }
        }
    }

    public void notifySetupReadiness() {
        String shipsPositionsString = NetworkUtils.shipsArrayToString(localEngine.getMyShipPositions());
        networkHandler.notifySetupReadiness(shipsPositionsString);
    }

    public void handleBoardClick(BoardType boardType, Point point) {
        switch (boardType) {
            case SETUP_BOARD -> handleSetupClick(point);
            case FOE_BOARD -> handleShotClick(point);
        }
    }

    private void handleSetupClick(Point point) {
        localEngine.toggleMyShipAt(point);
        shipsSetupClickCallback.accept(point);
    }

    private void handleShotClick(Point point) {
        boolean targetNotShot = !localEngine.isShot(FOE_BOARD, point);
        if (localEngine.isMyTurn() && targetNotShot) {
            proceedShot(FOE_BOARD, point);
        }
    }

    public void proceedShot(BoardType targetBoard, Point point) {
        localEngine.shotAt(targetBoard, point);
        drawShotCallback.accept(targetBoard, point);
        if (FOE_BOARD.equals(targetBoard)) {
            networkHandler.sendMessage(NetworkUtils.pointToShotMsg(point));
        }
        if (localEngine.endConditionsMet()) {
            gameStats.saveFinalState(localEngine.getBoardState(PLAYER_BOARD), localEngine.getBoardState(FOE_BOARD));
            gotoSummaryFunction.run();
        } else {
            turnLabelCallback.accept(localEngine.isMyTurn());
        }
    }

    public void setShipsSetupClickCallback(Consumer<Point> shipsSetupClickCallback) {
        this.shipsSetupClickCallback = shipsSetupClickCallback;
    }

    public void setTurnLabelCallback(Consumer<Boolean> turnLabelCallback) {
        this.turnLabelCallback = turnLabelCallback;
    }

    public void setDrawShotCallback(BiConsumer<BoardType, Point> drawShotCallback) {
        this.drawShotCallback = drawShotCallback;
    }

    public Predicate<Point> getIsShipFunction(BoardType boardType) {
        return switch (boardType) {
            case SETUP_BOARD, PLAYER_BOARD -> localEngine::isMyShip;
            case FOE_BOARD -> localEngine::isFoeShip;
        };
    }

    public Function<BoardType, Integer> getCountSunkenShipsFunction() {
        return localEngine::countSunkenShips;
    }

    public Boolean isServer() {
        if (networkHandler instanceof ServerHandler) {
            return true;
        } else if (networkHandler instanceof ClientHandler) {
            return false;
        }
        return null;
    }

    public boolean isMyTurn() {
        return localEngine.isMyTurn();
    }

    public void setNetworkHandler(SocketNetworkHandler handler) {
        if (Objects.nonNull(this.networkHandler)) {
            throw new IllegalStateException("NetworkHandler już istnieje.");
        }
        localEngine.setMyTurn(handler instanceof ServerHandler);
        this.networkHandler = handler;
    }

    public void setOpponentShipsState(Boolean[][] shipsState) {
        localEngine.saveOpponentShips(shipsState);
    }

    public int getBoardSize() {
        return localEngine.getBoardSize();
    }

    public int getShipsPerBoardQty() {
        return localEngine.getShipsQty();
    }

    public void startTimer() {
        this.gameStats = new GameStats();
    }

    public GameStats getStats() {
        return this.gameStats;
    }

    public boolean isClientConnected() {
        if (networkHandler instanceof ServerHandler serverHandler) {
            return serverHandler.isClientConnected();
        }
        return true;
    }
}
