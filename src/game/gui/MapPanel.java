package game.gui;

import game.controller.GameController;
import game.observer.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The MapPanel class represents the game grid UI.
 * It displays the game map using a grid of buttons, each corresponding to a cell in the game map.
 * It also responds to user interactions such as mouse clicks and arrow key presses,
 * and updates based on game state changes by implementing the GameObserver interface.
 */
public class MapPanel extends JPanel implements GameObserver {

    /**
     * Constructs a MapPanel with a grid layout and initializes cell buttons
     * with their icons and event listeners for user interaction.
     * @param controller the GameController responsible for managing game logic and actions
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
                button.addActionListener(e -> controller.handleLeftClick(finalRow, finalCol));
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent e) {
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
     * Sets up key bindings for arrow key navigation using the keyboard.
     * Arrow keys trigger corresponding actions in the GameController.
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
     * Refreshes the grid by updating all cell icons based on the current game state.
     */
    public void refresh() {
        for (int row = 0; row < cellButtons.length; row++) {
            for (int col = 0; col < cellButtons[0].length; col++) {
                cellButtons[row][col].setIcon(controller.getIconWithHealthBar(row, col));
            }
        }
    }

    /**
     * Temporarily highlights a specific cell with the given color, then resets it.
     * @param row   the row index of the cell to highlight
     * @param col   the column index of the cell to highlight
     * @param color the color to use for highlighting
     */
    public void highlightCell(int row, int col, Color color) {
        JButton cell = cellButtons[row][col];
        Color original = cell.getBackground();
        cell.setBackground(color);

        new javax.swing.Timer(300, e -> cell.setBackground(original)).start();
    }

    /**
     * Called when the game state is updated.
     * Triggers a refresh of the map grid.
     */
    @Override
    public void onGameUpdated() {
        refresh();
        System.out.println("MapPanel updated.");

    }
    // --- Fields ---
    private final JButton[][] cellButtons;
    private final GameController controller;
}
