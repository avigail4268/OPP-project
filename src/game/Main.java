/**
 * avigail musai 322227711
 * rotem dino 209168442
 * chira borochov 345887046
 */

package game;
import game.gui.GameSetUp;
/**
 * Entry point for the Dungeons & Dragons game.
 * Initializes user settings (map size, player type, name), game world, controller, and GUI.
 */
public class Main {

    /**
     * Launches the game application. Prompts the user for game configuration
     * and initializes the game world and GUI.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        GameSetUp gameSetUp = new GameSetUp();
        gameSetUp.start();
    }

}
