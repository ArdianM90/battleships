package app.project.controller.networking;

import app.project.model.types.BoardType;
import app.project.model.types.GameInitData;
import app.project.model.GameSettings;
import app.project.utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static app.project.model.types.BoardType.PLAYER_BOARD;

public class ClientHandler extends Thread implements SocketNetworkHandler {

    private final String host;
    private final Runnable goToSetupFunction;
    private final Runnable goToGameFunction;
    private final Runnable showErrorFunction;
    private final Consumer<GameInitData> setOpponentDataFunction;
    private final BiConsumer<BoardType, Point> receiveShotFunction;

    private Socket socket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    public ClientHandler(GameSettings settings, Runnable goToSetupFunction, Runnable goToGameFunction, Runnable showErrorFunction, Consumer<GameInitData> setOpponentDataFunction, BiConsumer<BoardType, Point> receiveShotFunction) {
        this.socket = null;
        this.host = settings.getHost();
        this.goToSetupFunction = goToSetupFunction;
        this.goToGameFunction = goToGameFunction;
        this.showErrorFunction = showErrorFunction;
        this.setOpponentDataFunction = setOpponentDataFunction;
        this.receiveShotFunction = receiveShotFunction;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, GameSettings.PORT);
            initConnection(socket);
            SwingUtilities.invokeLater(goToSetupFunction);
            serverMessagesListenerThread().start();
        } catch (IOException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
            SwingUtilities.invokeLater(showErrorFunction);
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
                        GameInitData opponentData = NetworkUtils.readyMsgToInitData(msg);
                        setOpponentDataFunction.accept(opponentData);
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
    public void notifySetupReadiness(String playerName, String shipsStateMsg) {
        outputStream.println("READY["+shipsStateMsg+"];"+playerName);
    }
}
