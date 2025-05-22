package app.project;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class ServerHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final Runnable goToGameFunction;

    private volatile boolean serverSetupReady = false;
    private volatile boolean clientSetupReady = false;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public ServerHandler(int port, Runnable goToGameFunction) {
        this.port = port;
        this.goToGameFunction = goToGameFunction;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Klient połączony.");
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = inputStream.readLine()) != null) {
                        if ("READY".equals(msg)) {
                            System.out.println("Klient wysłał READY");
                            clientSetupReady = true;
                            checkAndStartGame();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Błąd odczytu od klienta: " + e.getMessage());
                }
            }).start();

            while (!serverSetupReady) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }

    @Override
    public void notifySetupReadiness() {
        System.out.println("Wysyłam READY");
        serverSetupReady = true;
        checkAndStartGame();
    }

    private synchronized void checkAndStartGame() {
        if (serverSetupReady && clientSetupReady) {
            System.out.println("Obaj gracze gotowi. Wysyłam START");
            outputStream.println("START");
            SwingUtilities.invokeLater(goToGameFunction);
        }
    }
}