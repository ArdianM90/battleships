package app.project.controller.networking;

public interface SocketNetworkHandler {

    void sendMessage(String msg);

    void notifySetupReadiness(String playerName, String shipsStateMsg);

    void closeConnection();
}
