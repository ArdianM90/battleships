package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.networking.SocketNetworkHandler;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.*;

public class GameController {

    private final GameEngine localEngine;
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> handleMarkShipFunction;
    private BiConsumer<Boolean, Point> handleMarkShotFunction;

    public GameController(int boardSize) {
        localEngine = new GameEngine(boardSize);
    }

    public BiPredicate<Point, BoardType> isShipFunction() {
        return localEngine.isShipFunction();
    }

    private void handleBoardClickFunction(BoardType boardType, Point point) {
        switch (boardType) {
            case SETUP_BOARD:
                handleMarkShipFunction.accept(point);
                break;
            case PLAYER_BOARD:
            case FOE_BOARD:
                handleMarkShotFunction.accept(boardType.equals(BoardType.FOE_BOARD), point);
                break;
            default:
                throw new IllegalArgumentException("Nieobsługiwany boardType: " + boardType);
        }
    }

    public void notifySetupReadiness() {
        networkHandler.notifySetupReadiness();
    }

    public void setNetworkHandler(SocketNetworkHandler handler) {
        this.networkHandler = handler;
    }

    public int getBoardSize() {
        return localEngine.getBoardSize();
    }

    public void setHandleMarkShipFunction(Consumer<Point> markShipFunction) {
        this.handleMarkShipFunction = (point) -> {
            localEngine.setShipAt(point);
            markShipFunction.accept(point);
            networkHandler.sendMessage("SHIP[" + point.x + "," + point.y + "]");
        };
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

    public BiConsumer<Point, BoardType> getHandleBoardClickFunction() {
        return (point, boardType) -> handleBoardClickFunction(boardType, point);
    }
}
