package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.networking.NetworkUtils;
import app.project.controller.networking.SocketNetworkHandler;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.*;

import static app.project.model.BoardType.FOE_BOARD;

public class GameController {

    private final GameEngine localEngine;
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> setupBoardOnClickCallback;
    private BiConsumer<Boolean, Point> handleMarkShotFunction;

    public GameController(int boardSize) {
        localEngine = new GameEngine(boardSize);
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
        setupBoardOnClickCallback.accept(point);
    }

    public void setSetupBoardClickCallback(Consumer<Point> setupBoardClickCallback) {
        this.setupBoardOnClickCallback = setupBoardClickCallback;
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

    public int getBoardSize() {
        return localEngine.getBoardSize();
    }
}
