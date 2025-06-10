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

    public boolean getIsShip(int x, int y) {
        return shipCells[x][y].isShip();
    }
}
