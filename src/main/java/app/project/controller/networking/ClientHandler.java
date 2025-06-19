package app.project.controller.networking;

import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static app.project.model.BoardType.PLAYER_BOARD;

public class ClientHandler extends Thread implements SocketNetworkHandler {

    private final int port;
    private final String host;
    private final Runnable goToGameFunction;
    private final Consumer<Boolean[][]> setOpponentShipsFunction;
    private final BiConsumer<BoardType, Point> receiveShotFunction;

    private Socket socket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    public ClientHandler(String host, int port, Runnable goToGameFunction, Consumer<Boolean[][]> setOpponentShipsFunction, BiConsumer<BoardType, Point> receiveShotFunction) {
        this.socket = null;
        this.host = host;
        this.port = port;
        this.goToGameFunction = goToGameFunction;
        this.setOpponentShipsFunction = setOpponentShipsFunction;
        this.receiveShotFunction = receiveShotFunction;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            initConnection(socket);
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
                while (Objects.nonNull(msg = inputStream.readLine())) {
                    System.out.println("Klient otrzymał: " + msg);
                    if (msg.equals("START")) {
                        SwingUtilities.invokeLater(goToGameFunction);
                    } else if (msg.startsWith("READY")) {
                        Boolean[][] opponentShips = NetworkUtils.readyMsgToShipsArray(msg);
                        setOpponentShipsFunction.accept(opponentShips);
                    } else if (msg.startsWith("SHOT")) {
                        Point point = NetworkUtils.shotMsgToPoint(msg);
                        receiveShotFunction.accept(PLAYER_BOARD, point);
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
    public void notifySetupReadiness(String shipsStateMsg) {
        outputStream.println("READY["+shipsStateMsg+"]");
    }
}
