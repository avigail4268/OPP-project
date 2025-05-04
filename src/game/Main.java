package game;
import game.engine.GameWorld;
import game.controller.GameController;
import game.gui.GameFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = askMapSize();
            int playerType = askPlayerType();
            String name = askPlayerName();
            GameWorld world = new GameWorld(size, playerType, name);
            int panelSize = 640;
            int tileSize = panelSize / size;
            GameController controller = new GameController(world);
            controller.setTileSize(tileSize);

            GameFrame frame = new GameFrame(controller);
            controller.setFrame(frame);
            frame.setVisible(true);
        });
    }
    private static int askMapSize() {
        String input = JOptionPane.showInputDialog("Enter map size (min 10):");
        try {
            int size = Integer.parseInt(input);
            return Math.max(size, 10);
        } catch (Exception e) {
            return 10;
        }
    }
    private static int askPlayerType() {
        Object[] options = {"Warrior", "Mage", "Archer"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose type:",
                "Select Player Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, null);
        return choice + 1; // Warrior = 1, Mage = 2, Archer = 3
    }
    private static String askPlayerName() {
        return JOptionPane.showInputDialog("Enter your character's name:");
    }
}