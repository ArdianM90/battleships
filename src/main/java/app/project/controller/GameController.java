package app.project.controller;

import app.project.controller.local.GameEngine;
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
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> shipsSetupClickCallback;
    private BiConsumer<BoardType, Point> drawShotCallback;
    private Consumer<Boolean> showMyTurnLabelCallback;

    public GameController(int boardSize, int shipsQty) {
        localEngine = new GameEngine(boardSize, shipsQty);
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
                    handleMyShot(point);
                }
            }
            default -> throw new IllegalArgumentException("Niepoprawny typ planszy: " + boardType);
        }
    }

    private void handleShipSetup(Point point) {
        localEngine.toggleMyShipAt(point);
        shipsSetupClickCallback.accept(point);
    }

    private void handleMyShot(Point point) {
        boolean success = localEngine.saveShotAt(FOE_BOARD, point);
        if (success) {
            drawShotCallback.accept(FOE_BOARD, point);
            String shotMsg = NetworkUtils.pointToShotMsg(point);
            networkHandler.sendMessage(shotMsg);
            showMyTurnLabelCallback.accept(false);
            localEngine.setMyTurn(false);
        }
    }

    public void handleOpponentShot(Point point) {
        boolean success = localEngine.saveShotAt(PLAYER_BOARD, point);
        if (success) {
            drawShotCallback.accept(PLAYER_BOARD, point);
            showMyTurnLabelCallback.accept(true);
            localEngine.setMyTurn(true);
        }
    }

    public void setShipsSetupClickCallback(Consumer<Point> shipsSetupClickCallback) {
        this.shipsSetupClickCallback = shipsSetupClickCallback;
    }

    public void setMyTurnLabelCallback(Consumer<Boolean> showMyTurnLabelCallback) {
        this.showMyTurnLabelCallback = showMyTurnLabelCallback;
    }

    public void setDrawShotCallback(BiConsumer<BoardType, Point> drawShotCallback) {
        this.drawShotCallback = drawShotCallback;
    }

    public BiPredicate<BoardType, Point> getIsShipFunction() {
        return localEngine::isShip;
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
}
