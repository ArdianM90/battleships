package app.project;

public class Main {

    public static void main(String[] args) {
        boolean testMode = false;
        if (args.length > 0 && args[0].equals("--test")) {
            testMode = true;
        }
        new Battleships(testMode);
    }
}