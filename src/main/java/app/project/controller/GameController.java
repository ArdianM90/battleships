package app.project.controller;

import app.project.controller.local.GameEngine;
import app.project.controller.local.GameStats;
import app.project.controller.networking.*;
import app.project.model.BoardType;
import app.project.model.GameInitData;

import java.awt.*;
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

    public void createServer(int port, Runnable goToGameFunction, Runnable goToSetupFunction) {
        ServerHandler server = new ServerHandler(port, goToGameFunction, this::setOpponentData, this::proceedShot);
        server.start();
        setNetworkHandler(server);
        goToSetupFunction.run();
    }

    public void createClientSocket(String host, int port, Runnable goToSetupFunction, Runnable goToGameFunction, Runnable showErrorFunction) {
        ClientHandler client = new ClientHandler(host, port, goToSetupFunction, goToGameFunction, showErrorFunction, this::setOpponentData, this::proceedShot);
        client.start();
        setNetworkHandler(client);
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

    public void notifySetupReadiness(String playerName) {
        String shipsPositionsString = NetworkUtils.shipsArrayToString(localEngine.getMyShipPositions());
        networkHandler.notifySetupReadiness(playerName, shipsPositionsString);
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
        localEngine.setMyTurn(handler instanceof ServerHandler);
        this.networkHandler = handler;
    }

    public void setOpponentData(GameInitData opponentData) {
        localEngine.setOpponentName(opponentData.name());
        localEngine.saveOpponentShips(opponentData.shipsArray());
    }

    public void setPlayerName(String name) {
        localEngine.setPlayerName(name);
    }

    public String getOpponentName() {
        return localEngine.getOpponentName();
    }

    public int getBoardSize() {
        return localEngine.getBoardSize();
    }

    public int getShipsPerBoardQty() {
        return localEngine.getShipsQty();
    }

    public void startTimer() {
        this.gameStats = new GameStats(localEngine.getPlayerName(), localEngine.getOpponentName());
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
