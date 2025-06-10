package app.project.controller.networking;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.BiConsumer;

public class ClientHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final String host;
    private final Runnable goToGameFunction;

    private boolean gameStarted = false;

    private Socket socket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private BiConsumer<Point, Boolean> receiveShotFunction;

    public ClientHandler(String host, int port, Runnable goToGameFunction) {
        this.socket = null;
        this.host = host;
        this.port = port;
        this.goToGameFunction = goToGameFunction;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            initConnection(socket);
            String msg;
            do {
                msg = inputStream.readLine();

            } while (!"START".equals(msg));
            SwingUtilities.invokeLater(goToGameFunction);
            // todo: poprawić wymianę info o strzałach
            serverMessagesListenerThread().start();
        } catch (IOException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initConnection(Socket socket) throws IOException {
        System.out.println("Połączono z serwerem.");
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new PrintWriter(socket.getOutputStream(), true);
    }

    private Thread serverMessagesListenerThread() {
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = inputStream.readLine()) != null) {
                    if (msg.equals("START")) {
                        gameStarted = true;
                        System.out.println("Otrzymano START. Przechodzę do gry.");
                        SwingUtilities.invokeLater(goToGameFunction);
                    } else if (msg.startsWith("SHOT")) {
                        System.out.println("SHOT message received");
                    }
                }
            } catch (IOException e) {
                System.err.println("Błąd odczytu od serwera: " + e.getMessage());
            }
        });
        listenerThread.setDaemon(true);
        return listenerThread;
    }

    @Override
    public void sendMessage(String msg) {
        System.out.println("Klient wysyła wiadomość: " + msg);
        outputStream.println(msg);
    }

    @Override
    public void notifySetupReadiness() {
        outputStream.println("READY");
    }
}
