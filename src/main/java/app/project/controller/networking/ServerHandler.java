package app.project.controller.networking;

import javax.swing.*;
import java.io.*;
import java.awt.Point;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.function.BiConsumer;

public class ServerHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final Runnable goToGameFunction;

    private volatile boolean serverSetupReady = false;
    private volatile boolean clientSetupReady = false;
    private volatile boolean isServerTurn = true;
    private boolean gameStarted = false;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private BiConsumer<Point, Boolean> receiveShotFunction;

    public ServerHandler(int port, Runnable goToGameFunction) {
        this.port = port;
        this.goToGameFunction = goToGameFunction;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
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
    public void notifySetupReadiness() {
        System.out.println("Wysyłam READY");
        serverSetupReady = true;
        startGameIfBothReady();
    }

    @Override
    public void setReceiveShotFunction(BiConsumer<Point, Boolean> receiveShotFunction) {
        this.receiveShotFunction = receiveShotFunction;
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
                    if (msg.equals("READY")) {
                        System.out.println("Klient wysłał READY");
                        clientSetupReady = true;
                        startGameIfBothReady();
                    } else if (msg.startsWith("SHOT:")) {
                        if (!isServerTurn) {
                            String[] parts = msg.substring(5).split(",");
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            System.out.println("Klient strzela na [" + x + ", " + y +"]");
                            receiveShotFunction.accept(new Point(x, y), true);
                        }
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
