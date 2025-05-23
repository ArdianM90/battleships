package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.networking.SocketNetworkHandler;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class GameController {

    private GameEngine localGameEngine;
    private SocketNetworkHandler networkHandler;

    public GameController(int boardSize) {
        localGameEngine = new GameEngine(boardSize);
    }

    public BiPredicate<Point, Boolean> isShipFunction() {
        return localGameEngine.isShipFunction();
    }

    public BiConsumer<Point, Boolean> toggleShipFunction() {
        return localGameEngine.toggleShipFunction();
    }

    public void setMyBoardShotFunction(Consumer<Point> pointConsumer) {
        localGameEngine.setMyBoardShotFunction(pointConsumer);
    }

    public void setFoeBoardShotFunction(Consumer<Point> pointConsumer) {
        localGameEngine.setFoeBoardShotFunction(pointConsumer);
    }

    public BiConsumer<Point, Boolean> markShotFunction() {
        return localGameEngine.markShotFunction();
    }

    public void setNetworkHandler(SocketNetworkHandler handler) {
        this.networkHandler = handler;
    }

    public int getBoardSize() {
        return localGameEngine.getBoardSize();
    }

    public void notifySetupReadiness() {
        networkHandler.notifySetupReadiness();
    }
}
