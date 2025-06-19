package app.project.controller.networking;

import app.project.model.BoardType;

import javax.swing.*;
import java.io.*;
import java.awt.Point;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static app.project.model.BoardType.PLAYER_BOARD;

public class ServerHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final Runnable goToGameFunction;
    private final Consumer<Boolean[][]> setOpponentShipsFunction;
    private final BiConsumer<BoardType, Point> receiveShotFunction;

    private volatile boolean serverSetupReady = false;
    private volatile boolean clientSetupReady = false;
    private boolean gameStarted = false;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public ServerHandler(int port, Runnable goToGameFunction, Consumer<Boolean[][]> setOpponentShipsFunction, BiConsumer<BoardType, Point> receiveShotFunction) {
        this.serverSocket = null;
        this.clientSocket = null;
        this.port = port;
        this.goToGameFunction = goToGameFunction;
        this.setOpponentShipsFunction = setOpponentShipsFunction;
        this.receiveShotFunction = receiveShotFunction;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            initConnection(serverSocket);
            clientMessagesListenerThread().start();
            while (!serverSetupReady) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }

    private void initConnection(ServerSocket serverSocket) throws IOException {
        clientSocket = serverSocket.accept();
        System.out.println("Klient połączony.");
        inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private Thread clientMessagesListenerThread() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while (Objects.nonNull(msg = inputStream.readLine())) {
                    System.out.println("Serwer otrzymał: " + msg);
                    if (msg.startsWith("READY")) {
                        Boolean[][] opponentShips = NetworkUtils.readyMsgToShipsArray(msg);
                        setOpponentShipsFunction.accept(opponentShips);
                        clientSetupReady = true;
                        startGameIfBothReady();
                    } else if (msg.startsWith("SHOT")) {
                        Point point = NetworkUtils.shotMsgToPoint(msg);
                        receiveShotFunction.accept(PLAYER_BOARD, point);
                    }
                }
            } catch (IOException e) {
                System.err.println("Błąd odczytu od klienta: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        return listenerThread;
    }

    private synchronized void startGameIfBothReady() {
        if (!gameStarted && serverSetupReady && clientSetupReady) {
            gameStarted = true;
            System.out.println("Obaj gracze gotowi. Wysyłam START");
            outputStream.println("START");
            SwingUtilities.invokeLater(goToGameFunction);
        }
    }

    public boolean isClientConnected() {
        return Objects.nonNull(clientSocket);
    }

    @Override
    public void sendMessage(String msg) {
        System.out.println("Serwer wysyła wiadomość: " + msg);
        outputStream.println(msg);
    }

    @Override
    public void notifySetupReadiness(String shipsStateMsg) {
        serverSetupReady = true;
        sendMessage("READY["+shipsStateMsg+"]");
        startGameIfBothReady();
    }
}
