package app.project.view;

import javax.swing.*;
import java.awt.*;

public class ShipTile extends JPanel {
    private static final int SHOT_MARK_GAP = 8;
    private boolean isShot = false;

    public ShipTile() {
        setBackground(Color.BLUE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setOpaque(true);
    }

    public void setShip(boolean isShip) {
        setBackground(isShip ? Color.RED : Color.BLUE);
    }

    public void markShot() {
        this.isShot = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isShot) {
            Graphics2D graphics = (Graphics2D) g;
            graphics.setColor(Color.WHITE);
            graphics.setStroke(new BasicStroke(5));
            graphics.drawLine(SHOT_MARK_GAP, SHOT_MARK_GAP, getWidth() - SHOT_MARK_GAP, getHeight() - SHOT_MARK_GAP);
            graphics.drawLine(getWidth() - SHOT_MARK_GAP, SHOT_MARK_GAP, SHOT_MARK_GAP, getHeight() - SHOT_MARK_GAP);
        }
    }
}
