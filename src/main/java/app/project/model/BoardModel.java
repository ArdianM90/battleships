package app.project.model;

import java.util.Arrays;

public class BoardModel {
    private BoardTileModel[][] shipCells;

    public BoardModel() {
        this.shipCells = new BoardTileModel[GameSettings.BOARD_SIZE][GameSettings.BOARD_SIZE];
        for (int row = 0; row < GameSettings.BOARD_SIZE; row++) {
            for (int col = 0; col < GameSettings.BOARD_SIZE; col++) {
                shipCells[row][col] = new BoardTileModel();
            }
        }
    }

    public boolean[][] getShipPositions() {
        int boardSize = shipCells.length;
        boolean[][] boardStateArr = new boolean[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                boardStateArr[row][col] = shipCells[row][col].isShip();
            }
        }
        return boardStateArr;
    }

    public int countHitShips() {
        return Arrays.stream(shipCells)
                .flatMap(Arrays::stream)
                .mapToInt(e -> (e.isShip() && e.isHit()) ? 1 : 0)
                .sum();
    }

    public void toggleIsShip(int x, int y) {
        shipCells[x][y].toggleIsShip();
    }

    public void setShip(int x, int y) {
        shipCells[x][y].setIsShip(true);
    }

    public boolean isShip(int x, int y) {
        return shipCells[x][y].isShip();
    }

    public boolean isShot(int x, int y) {
        return shipCells[x][y].isHit();
    }

    public boolean shot(int x, int y) {
        if (shipCells[x][y].isHit()) {
            return false;
        }
        shipCells[x][y].setHit();
        return shipCells[x][y].isShip();
    }

    public void print(boolean myShips) {
        System.out.println((myShips ? "My" : "Foe") + " ships:");
        for (int row = 0; row < shipCells.length; row++) {
            for (int col = 0; col < shipCells.length; col++) {
                System.out.print((shipCells[row][col].isShip() ? "O" : "-") + (shipCells[row][col].isHit() ? "X " : "- "));
            }
            System.out.println();
        }
    }

    public BoardTileModel[][] getCells() {
        return shipCells;
    }
}
