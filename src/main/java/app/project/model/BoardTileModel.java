package app.project.model;

public class BoardTileModel {
    private boolean isShip;
    private boolean isHit;

    public boolean isShip() {
        return isShip;
    }

    public void toggleIsShip() {
        isShip = !isShip;
    }

    public void setIsShip(boolean ship) {
        isShip = ship;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit() {
        isHit = true;
    }
}
