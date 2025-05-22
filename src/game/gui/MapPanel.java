
package game.gui;

import game.controller.GameController;
import game.observer.GameObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The MapPanel class represents the UI component displaying the game grid.
 * It uses a JButton grid to visually represent map cells and respond to user interactions.
 * Implements GameObserver to support automatic UI refresh on game state changes.
 */
public class MapPanel extends JPanel implements GameObserver {

    /**
     * Constructs the MapPanel by initializing the grid layout and populating it
     * with interactive buttons, each representing a game cell.
     *
     * @param controller the GameController responsible for coordinating UI and logic
     */
    public MapPanel(GameController controller) {
        this.controller = controller;
        int rows = controller.getMapRows();
        int cols = controller.getMapCols();

        setLayout(new GridLayout(rows, cols));
        cellButtons = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton button = new JButton();
                button.setIcon(controller.getIconWithHealthBar(row, col));
                int finalRow = row;
                int finalCol = col;

                // Left click triggers action
                button.addActionListener(e -> controller.handleLeftClick(finalRow, finalCol));

                // Right click shows popup info
                button.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            controller.handleRightClick(finalRow, finalCol, button);
                        }
                    }
                });

                cellButtons[row][col] = button;
                add(button);
            }
        }

        setupKeyBindings();
    }

    /**
     * Defines key bindings for arrow keys to move the player via keyboard input.
     * Binds to UP, DOWN, LEFT, and RIGHT keys.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("UP");
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("DOWN");
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("LEFT");
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("RIGHT");
            }
        });
    }

    /**
     * Updates the entire grid by refreshing each tile's icon
     * according to the current state in the game world.
     */
    public void refresh() {
        for (int row = 0; row < cellButtons.length; row++) {
            for (int col = 0; col < cellButtons[0].length; col++) {
                cellButtons[row][col].setIcon(controller.getIconWithHealthBar(row, col));
            }
        }
    }

    /**
     * Highlights a specific tile with a temporary color (e.g. on action result).
     * After a short delay, the background is reset to its original color.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param color the color to highlight the cell with
     */
    public void highlightCell(int row, int col, Color color) {
        JButton cell = cellButtons[row][col];
        Color original = cell.getBackground();
        cell.setBackground(color);
        new Timer(300, e -> cell.setBackground(original)).start();
    }

    /**
     * Implementation of the GameObserver interface.
     * Called when the game state is updated.
     * Refreshes the grid display.
     */
    @Override
    public void onGameUpdated() {
        refresh();
    }

    // --- Fields ---

    /** 2D array of buttons representing map tiles. */
    private final JButton[][] cellButtons;

    /** Reference to the game controller. */
    private final GameController controller;
}
