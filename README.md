Readme PL -> [link](README.pl.md)

# Battleships – Classic Battleship Game (TCP Multiplayer)

> ℹ️ Note: The game's user interface is currently in Polish only, but all source code and class names are in English for better readability and maintainability.


This project is a classic 1 vs 1 battleship game with a graphical user interface (Swing) and TCP socket-based network communication. The application follows an MVC'ish' architecture and allows gameplay over a local network. It started as a simple assignment for a university course, but I kept developing it and turned into something I'm genuinely proud of.

## Installation and Launch

Requirements:
- Java 22+
- Maven

Build the project:
```bash
mvn clean package
```

Run the game:
```bash
java -jar target/battleships-1.0-SNAPSHOT.jar
```
or in test mode:
```bash
java -jar target/battleships-1.0-SNAPSHOT.jar --test
```

## How to Play?

The game consists of four screens:
1. **Main Menu** – choose a mode (host a game or join a server).
2. **Ship Setup** – place your ships by clicking on the board and enter your nickname.
3. **Gameplay** – players take turns shooting at the opponent’s board.
    - The host starts the game.
    - If you hit a ship, you continue your turn.
4. **Summary** – final screen with game statistics after the game ends.

Test mode:
When launched with the `--test` flag, the menu offers options for:
- automatic ship placement,
- opponent’s ships visibility.

## Technologies

The project follows an MVC-inspired structure:
- **Model** Classes representing game state – boards, statistics, player data, enums.
- **View** Swing components – GUI panels and game boards.
- **Controller** Game flow logic (GameController), networking (SocketNetworkHandler) and game mechanics (GameEngine).
- Functional interfaces (Java 8+) were used to enable clean communication between app components.

## Key Classes

- `Main.java`, `Battleships.java` – application entry point.
- `GameController.java` – manages game flow: handles user input, network connection (client/server), screen transitions, turn synchronization, and game state updates in cooperation with `GameEngine` and `SocketNetworkHandler`.
- `GameEngine.java` – local game logic: handles shots, board state management, win conditions, and turn control.
- `SocketNetworkHandler.java` – interface for TCP communication.
- `AppFrame.java` – main GUI window.
- `MainMenuPanel`, `ShipsSetupPanel`, `GameViewPanel`, `SummaryPanel` – individual game views.

## To do

- English translations for user interface,
- validation for ship placement (e.g. prevent touching, enforce size rules),
- implementation of Drag & Drop ship placement,
- Singleplayer mode with AI.

⭐ Feel free to star this repo if you found it useful.
