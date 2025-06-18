package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.local.GameStats;
import app.project.controller.networking.ClientHandler;
import app.project.controller.networking.NetworkUtils;
import app.project.controller.networking.ServerHandler;
import app.project.controller.networking.SocketNetworkHandler;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class GameController {

    private final GameEngine localEngine;
    private final GameStats gameStats;
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> shipsSetupClickCallback;
    private BiConsumer<BoardType, Point> drawShotCallback;
    private Consumer<Boolean> showTurnLabelCallback;
    private Runnable gotoSummaryFunction;

    public GameController(int boardSize, int shipsQty, Runnable gotoSummaryFunction) {
        this.localEngine = new GameEngine(boardSize, shipsQty);
        this.gotoSummaryFunction = gotoSummaryFunction;
        this.gameStats = new GameStats();
    }

    public void loadInitialShipsPositions(boolean[][] shipsSetup) {
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (shipsSetup[row][col]) {
                    handleShipSetup(new Point(row, col));
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
            case SETUP_BOARD -> handleShipSetup(point);
            case FOE_BOARD -> {
                if (localEngine.isMyTurn()) {
                    handleShot(FOE_BOARD, point);
                }
            }
        }
    }

    private void handleShipSetup(Point point) {
        localEngine.toggleMyShipAt(point);
        shipsSetupClickCallback.accept(point);
    }

    public void handleShot(BoardType targetBoard, Point point) {
        boolean success = localEngine.saveShotAt(targetBoard, point);
        boolean enemyShoot = PLAYER_BOARD.equals(targetBoard);
        if (success) {
            drawShotCallback.accept(targetBoard, point);
            if (!enemyShoot) {
                String shotMsg = NetworkUtils.pointToShotMsg(point);
                networkHandler.sendMessage(shotMsg);
            }
            showTurnLabelCallback.accept(enemyShoot);
            if (localEngine.endConditionsMet()) {
                gameStats.fillBoardStates(localEngine.getBoardState(PLAYER_BOARD), localEngine.getBoardState(FOE_BOARD));
                gotoSummaryFunction.run();
            } else {
                localEngine.setMyTurn(enemyShoot);
            }
        }
    }

    public void setShipsSetupClickCallback(Consumer<Point> shipsSetupClickCallback) {
        this.shipsSetupClickCallback = shipsSetupClickCallback;
    }

    public void setTurnLabelCallback(Consumer<Boolean> showTurnLabelCallback) {
        this.showTurnLabelCallback = showTurnLabelCallback;
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

    public void setNetworkHandler(SocketNetworkHandler handler) {
        if (this.networkHandler != null) {
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

    public GameStats getStats() {
        return this.gameStats;
    }
}
