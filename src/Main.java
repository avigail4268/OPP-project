
import game.engine.GameWorld;

/**
 * Rotem Dino - 209168442
 * Avigail Musai - 322227711
 * Chira Borochov - 345887046

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