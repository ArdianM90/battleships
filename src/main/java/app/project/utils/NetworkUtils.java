package app.project.utils;

import app.project.model.GameInitData;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetworkUtils {

    private static final String SHIP_MARK = "X";
    private static final String NO_SHIP_MARK = "O";

    public static GameInitData readyMsgToInitData(String msg) {
        String[] stringArr = extractShipsArray(msg);
        String opponentName = extractName(msg);
        int shipsArrSize = (int) Math.sqrt(stringArr.length);
        Boolean[][] shipsArray = new Boolean[shipsArrSize][shipsArrSize];
        for (int row = 0; row < shipsArrSize; row++) {
            for (int col = 0; col < shipsArrSize; col++) {
                shipsArray[row][col] = SHIP_MARK.equals(stringArr[row * shipsArrSize + col]);
            }
        }
        return new GameInitData(opponentName, shipsArray);
    }

    private static String[] extractShipsArray(String msg) {
        int beginIdx = msg.indexOf('[') + 1;
        int endIdx = msg.indexOf(']');
        return msg.substring(beginIdx, endIdx).split(",");
    }

    private static String extractName(String msg) {
        return msg.split(";")[1];
    }

    public static String shipsArrayToString(boolean[][] shipsState) {
        return Arrays.stream(shipsState)
                .flatMap(row -> IntStream.range(0, row.length).mapToObj(i -> row[i]))
                .map(b -> b ? SHIP_MARK : NO_SHIP_MARK)
                .collect(Collectors.joining(","));
    }

    public static Point shotMsgToPoint(String msg) {
        String[] stringPosition = msg.substring(msg.indexOf('[') + 1, msg.length() - 1).split(",");
        return new Point(Integer.parseInt(stringPosition[0]), Integer.parseInt(stringPosition[1]));
    }

    public static String pointToShotMsg(Point point) {
        return "SHOT[" + point.x + "," + point.y + "]";
    }
}
