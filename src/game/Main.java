package game;
import game.engine.GameWorld;
import game.controller.GameController;
import game.gui.GameFrame;
import javax.swing.*;

/**
 * The Main class serves as the entry point to the game application.
 * It initializes the game world, controller, and GUI frame,
 * and starts the game using user input for configuration.
 */
public class Main {

    /**
     * The main method that launches the game.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ask the user for map size (min 10)
            int size = askMapSize();

            // Ask the user for the type of player (Warrior, Mage, Archer)
            int playerType = askPlayerType();

            // Ask the user to enter a character name
            String name = askPlayerName();

            // Create the game world with the provided parameters
            GameWorld world = new GameWorld(size, playerType, name);

            // Define the fixed panel size in pixels
            int panelSize = 640;

            // Calculate tile size based on number of tiles (map size)
            int tileSize = panelSize / size;

            // Initialize the game controller with the world
            GameController controller = new GameController(world);
            controller.setTileSize(tileSize); // Set size of each tile

            // Create the GUI frame and link it to the controller
            GameFrame frame = new GameFrame(controller);
            controller.setFrame(frame);
            world.startEnemyTask();
            // Make the GUI visible
            frame.setVisible(true);
        });
    }

    /**
     * Prompts the user to input a map size via a dialog box.
     * Ensures that the returned size is at least 10.
     *
     * @return The validated map size (minimum 10)
     */
    private static int askMapSize() {
        String input = JOptionPane.showInputDialog("Enter map size (min 10):");
        try {
            int size = Integer.parseInt(input);
            return Math.max(size, 10); // Ensure minimum size is 10
        } catch (Exception e) {
            return 10; // Default to 10 on error or cancellation
        }
    }

    /**
     * Displays a dialog for the user to select a player type.
     *
     * @return An integer representing the player type:
     *         1 for Warrior, 2 for Mage, 3 for Archer
     */
    private static int askPlayerType() {
        Object[] options = {"Warrior", "Mage", "Archer"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose type:",                   // Message shown
                "Select Player Type",             // Dialog title
                JOptionPane.DEFAULT_OPTION,       // Type of options
                JOptionPane.QUESTION_MESSAGE,     // Icon type
                null, options, null);             // Options and default

        return choice + 1;
    }

    /**
     * Prompts the user to input a name for the player character.
     *
     * @return The name entered by the user
     */
    private static String askPlayerName() {
        return JOptionPane.showInputDialog("Enter your character's name:");
    }
}
