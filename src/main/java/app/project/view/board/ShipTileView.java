package app.project.view.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.Supplier;

public class ShipTileView extends JPanel {

    private final int SHOT_MARK_GAP;

    private boolean isShot = false;

    public ShipTileView(int size, Supplier<Boolean> drawAsShipFunction, Runnable notifyClickFunction) {
        SHOT_MARK_GAP = (int) Math.ceil(size / 4.0);
        setBackground(drawAsShipFunction.get() ? Color.RED : Color.BLUE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, (int) Math.ceil(size / 18.0)));
        setOpaque(true);
        setPreferredSize(new Dimension(size, size));
        if (Objects.nonNull(notifyClickFunction)) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    notifyClickFunction.run();
                }
            });
        }
    }

    public void setRed(boolean red) {
        setBackground(red ? Color.RED : Color.BLUE);
        repaint();
    }

    public void drawShot() {
        this.isShot = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isShot) {
            Graphics2D graphics = (Graphics2D) g;
            graphics.setColor(Color.WHITE);
            graphics.setStroke(new BasicStroke((int) Math.ceil(getWidth() / 7.0)));
            graphics.drawLine(SHOT_MARK_GAP, SHOT_MARK_GAP, getWidth() - SHOT_MARK_GAP, getHeight() - SHOT_MARK_GAP);
            graphics.drawLine(getWidth() - SHOT_MARK_GAP, SHOT_MARK_GAP, SHOT_MARK_GAP, getHeight() - SHOT_MARK_GAP);
        }
    }
}
