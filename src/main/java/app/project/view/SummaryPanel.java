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
    private final JLabel myShotsLabel = new JLabel("Twoje strzały", SwingConstants.CENTER);
    private final JLabel foeShotsLabel = new JLabel("Strzały przeciwnika", SwingConstants.CENTER);

    public SummaryPanel(int boardSize, GameStats stats) {
        this.myBoardView = new BoardView(PLAYER_BOARD, boardSize, stats.getBoardState(PLAYER_BOARD));
        this.foeBoardView = new BoardView(FOE_BOARD, boardSize, stats.getBoardState(FOE_BOARD));
        initComponents(stats);
    }

    private void initComponents(GameStats stats) {
        setName(SUMMARY.name());
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        JLabel winnerLabel = new JLabel(stats.isWinner() ? "Wygrałeś" : "Wygrał przeciwnik");
        winnerLabel.setFont(winnerLabel.getFont().deriveFont(Font.BOLD, 22f));
        winnerLabel.setForeground(stats.isWinner() ? Color.GREEN.darker() : Color.RED);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(winnerLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 120, 20));
        JLabel[] myDetails = fillDetailLabels(stats, PLAYER_BOARD);
        JLabel[] foeDetails = fillDetailLabels(stats, FOE_BOARD);
        boardsPanel.add(prepareColumn(foeBoardLabel, foeBoardView, myShotsLabel, foeDetails));
        boardsPanel.add(prepareColumn(myBoardLabel, myBoardView, foeShotsLabel, myDetails));
        add(boardsPanel, BorderLayout.CENTER);

        long seconds = stats.getGameTimeSeconds();
        JLabel timeLabel = new JLabel(String.format("Czas gry: %02d:%02d", seconds / 60, seconds % 60));
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 18f));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
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

    private JPanel prepareColumn(JLabel boardLabel, BoardView board, JLabel detailsLabel, JLabel[] detailsArray) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        boardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardLabel.setFont(boardLabel.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(boardLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(board);
        panel.add(Box.createVerticalStrut(20));

        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsLabel.setFont(boardLabel.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(detailsLabel);
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
