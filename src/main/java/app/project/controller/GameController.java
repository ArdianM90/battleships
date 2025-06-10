package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.networking.SocketNetworkHandler;
import app.project.model.BoardType;

import java.awt.*;
import java.util.function.*;

public class GameController {

    private GameEngine localEngine;
    private SocketNetworkHandler networkHandler;
    private Consumer<Point> markShipFunction;
    private BiConsumer<Boolean, Point> markShotFunction;

    public GameController(int boardSize) {
        localEngine = new GameEngine(boardSize);
    }

    public BiPredicate<Point, BoardType> isShipFunction() {
        return localEngine.isShipFunction();
    }

    private void boardClickFunction(BoardType boardType, Point point) {
        switch (boardType) {
            case SETUP_BOARD:
                markShipFunction.accept(point);
                break;
            case PLAYER_BOARD:
            case FOE_BOARD:
                markShotFunction.accept(boardType.equals(BoardType.FOE_BOARD), point);
                break;
            default:
                throw new IllegalArgumentException("Unknown board type: " + boardType);
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

    public void setMarkShipFunction(Consumer<Point> markShipFunction) {
        this.markShipFunction = (point) -> {
            localEngine.setShipAt(point);
            markShipFunction.accept(point);
            System.out.println("Mark SHIP function");
            networkHandler.sendMessage("SHIP [" + point.x + ", " + point.y + "]");
        };
    }

    public void setMarkShotFunction(BiConsumer<Boolean, Point> markShotFunction) {
        this.markShotFunction = (foeBoard, point) -> {
            boolean success = localEngine.saveShotAt(foeBoard, point);
            System.out.println("Mark SHOT function, success: " + success);
            if (success) {
                markShotFunction.accept(foeBoard, point);
                networkHandler.sendMessage("SHOT [" + point.x + ", " + point.y + "]");
            }
        };
    }

    public BiConsumer<Point, BoardType> getNotifyClickFunction() {
        return (point, boardType) -> boardClickFunction(boardType, point);
    }
}
