package app.project.view;

import app.project.controller.local.GameStats;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.SUMMARY;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class SummaryPanel extends JPanel {

    private final BoardView myBoardView;
    private final BoardView foeBoardView;
    private final JLabel myBoardLabel;
    private final JLabel foeBoardLabel;

    public SummaryPanel(int boardSize, GameStats stats) {
        this.myBoardView = new BoardView(PLAYER_BOARD, boardSize, stats.getBoardState(PLAYER_BOARD));
        this.foeBoardView = new BoardView(FOE_BOARD, boardSize, stats.getBoardState(FOE_BOARD));
        this.myBoardLabel = new JLabel("Twoja plansza", SwingConstants.CENTER);
        this.foeBoardLabel = new JLabel("Plansza przeciwnika", SwingConstants.CENTER);
        initComponents();
    }

    private void initComponents() {
        setName(SUMMARY.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 120, 20));
        boardsPanel.add(boardWithLabel(myBoardLabel, myBoardView));
        boardsPanel.add(boardWithLabel(foeBoardLabel, foeBoardView));
        add(boardsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel boardWithLabel(JLabel label, BoardView board) {
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
