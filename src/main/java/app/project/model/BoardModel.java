package app.project.model;

public class BoardModel {
    private BoardCell[][] shipCells;

    public BoardModel(int size) {
        this.shipCells = new BoardCell[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                shipCells[row][col] = new BoardCell();
            }
        }
    }

    public void toggleIsShip(int x, int y) {
        shipCells[x][y].setIsShip(!shipCells[x][y].isShip());
    }

    public boolean getIsShip(int x, int y) {
        return shipCells[x][y].isShip();
    }
}
