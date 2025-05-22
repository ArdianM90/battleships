package app.project;

import java.io.*;
import java.net.*;

public class ServerHandler {
    public static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serwer uruchomiony. Oczekiwanie na klienta...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Klient połączony: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            System.out.println("Odebrano wiadomość od klienta: " + message);

            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }
}