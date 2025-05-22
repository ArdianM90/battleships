package app.project;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    public static void connectToServer(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);

            out.println("Wiadomość od klienta!");
            out.println("Druga wiadomość od klienta!");
            System.out.println("Wysłano wiadomość do serwera.");

        } catch (Exception e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }
}
