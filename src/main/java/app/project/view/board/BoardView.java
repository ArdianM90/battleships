package app.project.view.board;

import app.project.model.types.BoardType;

import javax.swing.*;
import java.awt.*;
import java.util.function.*;

import static app.project.model.types.BoardType.FOE_BOARD;

public abstract class BoardView extends JPanel {

    protected boolean showEnemyShips;
    protected BoardType boardType;
    protected ShipTileView[][] shipsArr;
    protected Predicate<Point> isShipFunction;
    protected BiConsumer<BoardType, Point> notifyClickFunction;

    public void toggleShip(Point point) {
        shipsArr[point.x][point.y].setRed(isShipFunction.test(point));
    }

    public void drawShot(Point point) {
        if (FOE_BOARD.equals(boardType) && isShipFunction.test(point)) {
            toggleShip(point);
        }
        shipsArr[point.x][point.y].drawShot();
    }

    protected Supplier<Boolean> getDrawTileAsShipFunction(int row, int col) {
        return () -> {
            if (!FOE_BOARD.equals(this.boardType) || showEnemyShips) {
                return this.isShipFunction.test(new Point(row, col));
            }
            return false;
        };
    }
}