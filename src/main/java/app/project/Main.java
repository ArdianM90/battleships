package app.project;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            new Battleships(true);
        } else {
            new Battleships(false);
        }
    }
}