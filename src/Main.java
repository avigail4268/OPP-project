import game.engine.GameWorld;

/**
 * Main class to start the game.
 * This class contains the main method which is the entry point of the program.
 * It creates an instance of GameWorld and starts the game.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(" Hello and welcome! ");
        GameWorld game = new GameWorld();
        game.startGame();
    }
}