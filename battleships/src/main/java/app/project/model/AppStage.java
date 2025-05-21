package app.project.model;

public enum AppStage {
    MAIN_MENU("menu"),
    SHIPS_SETUP("setup"),
    GAME("game");

    private String name;

    AppStage(String name) {
        this.name = name;
    }
}
