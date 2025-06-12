package app.project.controller.networking;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetworkUtils {

    private static final String SHIP_MARK = "X";
    private static final String NO_SHIP_MARK = "O";

    public static Boolean[][] readyMsgToShipsArray(String msg) {
        String[] stringArr = msg.substring(msg.indexOf('[') + 1, msg.length() - 1).split(",");
        int shipsArrSize = (int) Math.sqrt(stringArr.length);
        Boolean[][] result = new Boolean[shipsArrSize][shipsArrSize];
        for (int row = 0; row < shipsArrSize; row++) {
            for (int col = 0; col < shipsArrSize; col++) {
                result[row][col] = SHIP_MARK.equals(stringArr[row * shipsArrSize + col]);
            }
        }
        return result;
    }

    public static String shipsArrayToString(boolean[][] shipsState) {
        return Arrays.stream(shipsState)
                .flatMap(row -> IntStream.range(0, row.length).mapToObj(i -> row[i]))
                .map(b -> b ? SHIP_MARK : NO_SHIP_MARK)
                .collect(Collectors.joining(","));
    }
}
