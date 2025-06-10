package app.project.controller.networking;

public interface SocketNetworkHandler {

    void sendMessage(String msg);

    void notifySetupReadiness();
}
