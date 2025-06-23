package app.project.utils;

import app.project.model.GameSettings;
import app.project.model.GameStats;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

import static app.project.model.GameSettings.BOARD_SIZE;
import static app.project.model.types.BoardType.FOE_BOARD;
import static app.project.model.types.BoardType.PLAYER_BOARD;

public class FileUtils {

    public static boolean[][] loadShipPositions(String playerRole) {
        try (InputStream in = FileUtils.class.getResourceAsStream(GameSettings.INITIAL_SHIP_POSITIONS_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            String line;
            while ((line = reader.readLine()) != null)
                if (line.startsWith(playerRole)) {
                    int start = line.indexOf('[');
                    int end = line.indexOf(']');
                    if (start < 0 || end < 0 || end <= start) {
                        throw new IllegalArgumentException("Problem z danymi wejściowymi: " + line);
                    }
                    return mapPositionsStrToArray(line.substring(start + 1, end));
                }
        } catch (IOException e) {
            System.out.println("Błąd podczas czytania pliku: " + e.getMessage());
        }
        return null;
    }

    private static boolean[][] mapPositionsStrToArray(String positionsStr) {
        boolean[][] result = new boolean[BOARD_SIZE][BOARD_SIZE];
        if (positionsStr.length() != BOARD_SIZE * BOARD_SIZE) {
            throw new IllegalArgumentException(String.format("Długość tablicy niezgodna z rozmiarem planszy %d vs %d.", BOARD_SIZE * BOARD_SIZE, positionsStr.length()));
        }
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            int row = i / BOARD_SIZE;
            int col = i % BOARD_SIZE;
            result[row][col] = positionsStr.charAt(i) == '1';
        }
        return result;
    }

    public static void saveGameStatsFile(GameStats stats) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String fileSuffix = String.format("%06d", new Random().nextInt(1_000_000));
        String filePath = "./stats/" + dateStr + "_" + fileSuffix + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(dateStr);
            writer.newLine();
            writer.write(String.format("Wygrał gracz: %s", stats.getWinnerName()));
            writer.newLine();
            int accurateShots = stats.getAccurateShots(FOE_BOARD);
            int totalShots = stats.getTotalShots(FOE_BOARD);
            writer.write(String.format("Statystyki gracza %s: oddane/celne strzały - %d/%d, celność: %.2f%%, pozostałe statki: %d",
                    stats.getPlayerName(), totalShots, accurateShots, accurateShots * 100.0 / totalShots, stats.countFloatingShips(FOE_BOARD)));
            writer.newLine();
            accurateShots = stats.getAccurateShots(PLAYER_BOARD);
            totalShots = stats.getTotalShots(PLAYER_BOARD);
            writer.write(String.format("Statystyki gracza %s: oddane/celne strzały - %d/%d, celność: %.2f%%, pozostałe statki: %d",
                    stats.getPlayerName(), totalShots, accurateShots, accurateShots * 100.0 / totalShots, stats.countFloatingShips(PLAYER_BOARD)));
            writer.newLine();
            long seconds = stats.getGameTimeSeconds();
            writer.write(String.format("Czas gry: %02d:%02d", seconds / 60, seconds % 60));
            writer.newLine();
            System.out.println("Statystyki zapisane do pliku: " + filePath);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania statystyk do pliku: " + e.getMessage());
        }
    }
}
