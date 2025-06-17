package app.project.view;

import app.project.controller.GameController;
import app.project.controller.local.GameStats;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.SUMMARY;

public class SummaryPanel extends JPanel {

    private final BoardView myBoardView;
    private final BoardView foeBoardView;
    private final JLabel myBoardLabel;
    private final JLabel foeBoardLabel;

    public SummaryPanel(GameController gameController, GameStats stats) {
        this.myBoardView = BoardView.SummaryBoardFactory(gameController.getBoardSize(), gameController.getIsShipFunction());
        this.foeBoardView = BoardView.SummaryBoardFactory(gameController.getBoardSize(), gameController.getIsShipFunction());
        this.myBoardLabel = new JLabel("Twoja plansza", SwingConstants.CENTER);
        this.foeBoardLabel = new JLabel("Plansza przeciwnika", SwingConstants.CENTER);
        initComponents();
    }

    private void initComponents() {
        setName(SUMMARY.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        boardsPanel.add(createBoardWithLabel(myBoardLabel, myBoardView));
        boardsPanel.add(createBoardWithLabel(foeBoardLabel, foeBoardView));
        add(boardsPanel, BorderLayout.CENTER);
    }

    private JPanel createBoardWithLabel(JLabel label, BoardView board) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(board);
        return panel;
    }
}
