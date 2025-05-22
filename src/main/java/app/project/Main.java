package app.project;

public class Main {
    public static void main(String[] args) {
        AppWindow appWindow = new AppWindow();
    }

//    Oddziel GUI od logiki jeszcze mocniej — rozważ przeniesienie logiki kolorowania pól (czerwony/niebieski) do GameProcessor lub innego kontrolera.
//    Dodaj testy jednostkowe do GameProcessor i BoardModel.
//    Wyodrębnij dane startowe (setup) z GameProcessor do osobnej klasy/konfiguracji.
//    Zadbaj o rozróżnienie gracza i przeciwnika — np. nazwij plansze jawnie playerBoard, enemyBoard.
}