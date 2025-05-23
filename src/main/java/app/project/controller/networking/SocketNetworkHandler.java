package app.project.controller.networking;

import java.awt.*;
import java.util.function.BiConsumer;

public interface SocketNetworkHandler {

    void notifySetupReadiness();

    void sendShot(Point point);

    void setReceiveShotFunction(BiConsumer<Point, Boolean> receiveShotFunction);
}
