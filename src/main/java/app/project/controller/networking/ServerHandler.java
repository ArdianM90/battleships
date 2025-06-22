package app.project.controller.networking;

import app.project.model.types.BoardType;
import app.project.model.types.GameInitData;
import app.project.model.GameSettings;
import app.project.utils.NetworkUtils;

import javax.swing.*;
import java.io.*;
import java.awt.Point;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static app.project.model.types.BoardType.PLAYER_BOARD;

public class ServerHandler extends Thread implements SocketNetworkHandler {

    private final Runnable goToGameFunction;
    private final Consumer<GameInitData> setOpponentDataFunction;
    private final BiConsumer<BoardType, Point> receiveShotFunction;

    private volatile boolean serverSetupReady = false;
    private volatile boolean clientSetupReady = false;
    private boolean gameStarted = false;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public ServerHandler(Runnable goToGameFunction, Consumer<GameInitData> setOpponentDataFunction, BiConsumer<BoardType, Point> receiveShotFunction) {
        this.serverSocket = null;
        this.clientSocket = null;
        this.goToGameFunction = goToGameFunction;
        this.setOpponentDataFunction = setOpponentDataFunction;
        this.receiveShotFunction = receiveShotFunction;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(GameSettings.PORT);
            initConnection(serverSocket);
            clientMessagesListenerThread().start();
            while (!serverSetupReady) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            closeConnection();
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
                        GameInitData opponentData = NetworkUtils.readyMsgToInitData(msg);
                        setOpponentDataFunction.accept(opponentData);
                        clientSetupReady = true;
                        startGameIfBothReady();
                    } else if (msg.startsWith("SHOT")) {
                        Point point = NetworkUtils.shotMsgToPoint(msg);
                        receiveShotFunction.accept(PLAYER_BOARD, point);
                    }
                }
                closeConnection();
            } catch (IOException e) {
                closeConnection();
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
    public void notifySetupReadiness(String playerName, String shipsStateMsg) {
        serverSetupReady = true;
        sendMessage("READY["+shipsStateMsg+"];"+playerName);
        startGameIfBothReady();
    }

    @Override
    public void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
