package app.project;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final String host;
    private final Runnable goToGameFunction;

    private PrintWriter outputStream;
    private BufferedReader inputStream;

    public ClientHandler(String host, int port, Runnable goToGameFunction) {
        this.host = host;
        this.port = port;
        this.goToGameFunction = goToGameFunction;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {
            outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            do {
                msg = inputStream.readLine();
            } while (!"START".equals(msg));
            SwingUtilities.invokeLater(goToGameFunction);
        } catch (Exception e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }

    @Override
    public void notifySetupReadiness() {
        outputStream.println("READY");
    }
}