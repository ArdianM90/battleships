package app.project.view;

import app.project.controller.local.GameStats;
import app.project.model.BoardType;

import javax.swing.*;
import java.awt.*;

import static app.project.model.AppStage.SUMMARY;
import static app.project.model.BoardType.FOE_BOARD;
import static app.project.model.BoardType.PLAYER_BOARD;

public class SummaryPanel extends JPanel {

    private final BoardView myBoardView;
    private final BoardView foeBoardView;
    private final JLabel myBoardLabel = new JLabel("Twoja plansza", SwingConstants.CENTER);
    private final JLabel foeBoardLabel = new JLabel("Plansza przeciwnika", SwingConstants.CENTER);

    public SummaryPanel(int boardSize, GameStats stats) {
        this.myBoardView = new BoardView(PLAYER_BOARD, boardSize, stats.getBoardState(PLAYER_BOARD));
        this.foeBoardView = new BoardView(FOE_BOARD, boardSize, stats.getBoardState(FOE_BOARD));
        initComponents(stats);
    }

    private void initComponents(GameStats stats) {
        setName(SUMMARY.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 120, 20));
        JLabel[] myDetails = fillDetailLabels(stats, PLAYER_BOARD);
        JLabel[] foeDetails = fillDetailLabels(stats, FOE_BOARD);
        boardsPanel.add(boardWithLabel(myBoardLabel, myBoardView, myDetails));
        boardsPanel.add(boardWithLabel(foeBoardLabel, foeBoardView, foeDetails));
        add(boardsPanel, BorderLayout.CENTER);

        long seconds = stats.getGameTimeSeconds();
        JLabel timeLabel = new JLabel(String.format("Czas gry: %02d:%02d", seconds / 60, seconds % 60));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(timeLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JLabel[] fillDetailLabels(GameStats stats, BoardType boardType) {
        if (!PLAYER_BOARD.equals(boardType) && !FOE_BOARD.equals(boardType)) {
            throw new IllegalArgumentException("Nieobsługiwany typ planszy: " + boardType);
        }
        int totalShots = stats.getTotalShots(boardType);
        int accurateShots = stats.getAccurateShots(boardType);
        return new JLabel[]{
                new JLabel(String.format("Oddane strzały: %d", totalShots)),
                new JLabel(String.format("Celne strzały: %d", accurateShots)),
                new JLabel(String.format("Celność: %.2f%%", accurateShots * 100.0 / totalShots)),
                new JLabel(String.format("Pozostałe statki: %d", stats.countFloatingShips(boardType)))
        };
    }

    private JPanel boardWithLabel(JLabel label, BoardView board, JLabel[] detailsArray) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(board);
        panel.add(Box.createVerticalStrut(10));

        JPanel statsPanel = new JPanel(new GridLayout(detailsArray.length, 1, 0, 5));
        for (JLabel statLabel : detailsArray) {
            statLabel.setFont(statLabel.getFont().deriveFont(Font.PLAIN, 14f));
            statsPanel.add(statLabel);
        }
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statsPanel);

        return panel;
    }
}
