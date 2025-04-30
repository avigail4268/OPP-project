package game;

import game.engine.GameWorld;
import game.controller.GameController;
import game.gui.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // קלט מהמשתמש
            int size = askMapSize();
            int playerType = askPlayerType();
            String name = askPlayerName();

            // יצירת העולם והבקר
            GameWorld world = new GameWorld(size, playerType, name);
            GameController controller = new GameController(world);

            // יצירת ה־GUI
            GameFrame frame = new GameFrame(controller);
            controller.setFrame(frame);  // חיבור בין controller ל־frame
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
                "Choose your class:",
                "Select Player Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return choice + 1; // Warrior = 1, Mage = 2, Archer = 3
    }

    private static String askPlayerName() {
        return JOptionPane.showInputDialog("Enter your character's name:");
    }
}