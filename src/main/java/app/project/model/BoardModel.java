package app.project.model;

public class BoardModel {
    private BoardCellModel[][] shipCells;

    public BoardModel(int size) {
        this.shipCells = new BoardCellModel[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                shipCells[row][col] = new BoardCellModel();
            }
        }
    }

    public boolean shotAt(int x, int y) {
        shipCells[x][y].setIsHit(true);
        return true;
    }

    public void toggleIsShip(int x, int y) {
        shipCells[x][y].setIsShip(!shipCells[x][y].isShip());
    }

    public void setIsShip(int x, int y, boolean isShip) {
        shipCells[x][y].setIsShip(isShip);
    }

    public boolean getIsShip(int x, int y) {
        return shipCells[x][y].isShip();
    }

    public void print(boolean myShips) {
        System.out.println((myShips ? "My" : "Foe") + " ships:");
        for (int row = 0; row < shipCells.length; row++) {
            for (int col = 0; col < shipCells.length; col++) {
                System.out.print(shipCells[row][col].isShip() ? "X " : "- ");
            }
            System.out.println();
        }
    }

    public boolean[][] getBoardStateArray() {
        int boardSize = shipCells.length;
        boolean[][] boardStateArr = new boolean[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                boardStateArr[row][col] = shipCells[row][col].isShip();
            }
        }
        return boardStateArr;
    }
}
