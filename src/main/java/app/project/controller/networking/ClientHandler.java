package app.project.controller.networking;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

public class ClientHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final String host;
    private final Runnable goToGameFunction;

    private volatile boolean isClientTurn = false;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private BiConsumer<Point, Boolean> receiveShotFunction;

    public ClientHandler(String host, int port, Runnable goToGameFunction) {
        this.host = host;
        this.port = port;
        this.goToGameFunction = goToGameFunction;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {
            initConnection(socket);
            String msg;
            do {
                msg = inputStream.readLine();
            } while (!"START".equals(msg));
            SwingUtilities.invokeLater(goToGameFunction);
            // dodać uruchomienie listenera na info o strzałach
        } catch (Exception e) {
            System.err.println("Błąd klienta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void notifySetupReadiness() {
        outputStream.println("READY");
    }

    @Override
    public void sendShot(Point point) {
        outputStream.println("SHOT:" + point.x + "," + point.y);
        isClientTurn = false;
    }

    @Override
    public void setReceiveShotFunction(BiConsumer<Point, Boolean> receiveShotFunction) {
        this.receiveShotFunction = receiveShotFunction;
    }

    private void initConnection(Socket socket) throws IOException {
        outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
