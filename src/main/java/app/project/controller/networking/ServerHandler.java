package app.project.controller.networking;

import javax.swing.*;
import java.io.*;
import java.awt.Point;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class ServerHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final Runnable goToGameFunction;
    private final Consumer<Boolean[][]> setOpponentShipsFunction;
    private final Consumer<Point> receiveShotFunction;

    private volatile boolean serverSetupReady = false;
    private volatile boolean clientSetupReady = false;
    private boolean gameStarted = false;

    private ServerSocket serverSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public ServerHandler(int port, Runnable goToGameFunction, Consumer<Boolean[][]> setOpponentShipsFunction, Consumer<Point> receiveShotFunction) {
        this.serverSocket = null;
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
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String msg) {
        System.out.println("Serwer wysyła wiadomość: " + msg);
        outputStream.println(msg);
    }

    @Override
    public void notifySetupReadiness(String shipsStateMsg) {
        System.out.println("Jestem READY i wysyłam moje statki");
        serverSetupReady = true;
        sendMessage("READY["+shipsStateMsg+"]");
        startGameIfBothReady();
    }

    private void initConnection(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Klient połączony.");
        inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private Thread clientMessagesListenerThread() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = inputStream.readLine()) != null) {
                    System.out.println("Serwer otrzymał: " + msg);
                    if (msg.startsWith("READY")) {
                        Boolean[][] opponentShips = NetworkUtils.readyMsgToShipsArray(msg);
                        setOpponentShipsFunction.accept(opponentShips);
                        clientSetupReady = true;
                        startGameIfBothReady();
                    } else if (msg.startsWith("SHOT")) {
                        Point point = NetworkUtils.shotMsgToPoint(msg);
                        receiveShotFunction.accept(point);
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
}
