package app.project.model;

public class GameSettings {

    public static final int PORT = 12345;
    public static final int BOARD_SIZE = 12;
    public static final int SHIPS_QTY = 20;

    private boolean testMode;
    private boolean loadInitialShipsSetup = false;
    private boolean showEnemyShips = false;
    private String host;

    public GameSettings(boolean testMode) {
        this.testMode = testMode;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public boolean isLoadInitialShipsSetup() {
        return loadInitialShipsSetup;
    }

    public void setLoadInitialShipsSetup(boolean loadInitialShipsSetup) {
        this.loadInitialShipsSetup = loadInitialShipsSetup;
    }

    public boolean isShowEnemyShips() {
        return showEnemyShips;
    }

    public void setShowEnemyShips(boolean showEnemyShips) {
        this.showEnemyShips = showEnemyShips;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
