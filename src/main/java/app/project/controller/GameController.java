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

public class GameController {

    private final GameEngine localEngine;
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> shipsSetupClickCallback;
    private BiConsumer<Boolean, Point> handleMarkShotFunction;

    public GameController(int boardSize) {
        localEngine = new GameEngine(boardSize);
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

    public void handleBoardClick(BoardType boardType, Point point) {
        switch (boardType) {
            case SETUP_BOARD:
                handleShipSetup(point);
                break;
            case PLAYER_BOARD:
            case FOE_BOARD:
                handleMarkShotFunction.accept(FOE_BOARD.equals(boardType), point);
                break;
            default:
                throw new IllegalArgumentException("Nieobsługiwany boardType: " + boardType);
        }
    }

    public void notifySetupReadiness() {
        String shipsStateString = NetworkUtils.shipsArrayToString(localEngine.getMyBoardState());
        networkHandler.notifySetupReadiness(shipsStateString);
    }

    public void handleShipSetup(Point point) {
        localEngine.toggleMyShipAt(point);
        shipsSetupClickCallback.accept(point);
    }

    public void setShipsSetupClickCallback(Consumer<Point> shipsSetupClickCallback) {
        this.shipsSetupClickCallback = shipsSetupClickCallback;
    }

    public void setHandleMarkShotFunction(BiConsumer<Boolean, Point> markShotFunction) {
        this.handleMarkShotFunction = (foeBoard, point) -> {
            boolean success = localEngine.saveShotAt(foeBoard, point);
            if (success) {
                markShotFunction.accept(foeBoard, point);
                networkHandler.sendMessage("SHOT[" + point.x + "," + point.y + "]");
            }
        };
    }

    public void setNetworkHandler(SocketNetworkHandler handler) {
        this.networkHandler = handler;
    }

    public void setOpponentShipsState(Boolean[][] shipsState) {
        localEngine.saveOpponentShips(shipsState);
    }

    public BiPredicate<BoardType, Point> isShipFunction() {
        return localEngine.isShipFunction();
    }

    public Boolean isServer() {
        if (networkHandler instanceof ServerHandler) {
            return true;
        } else if (networkHandler instanceof ClientHandler) {
            return false;
        }
        return null;
    }

    public int getBoardSize() {
        return localEngine.getBoardSize();
    }
}
