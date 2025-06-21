package game.gui;
import game.controller.GameController;
import game.observer.GameObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The MapPanel class represents the game map in the GUI.
 * It displays a grid of buttons, each representing a cell in the game world.
 * The panel handles user interactions such as left and right clicks,
 * and provides methods to refresh the display and highlight cells.
 */
public class MapPanel extends JPanel implements GameObserver {

    /** Constructs a MapPanel with the specified game controller.
     * Initializes the grid of buttons and sets up event listeners for user interactions.
     * @param controller The game controller that manages the game state and player actions
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
     * Sets up key bindings for arrow keys to move the player character.
     * This allows the player to navigate the map using keyboard arrow keys.
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
     * Refreshes the map display by updating the icons of all buttons
     * based on the current game state.
     * This method is called whenever the game state changes to reflect updates.
     */
    public void refresh() {
        for (int row = 0; row < cellButtons.length; row++) {
            for (int col = 0; col < cellButtons[0].length; col++) {
                cellButtons[row][col].setIcon(controller.getIconWithHealthBar(row, col));
            }
        }
    }

    /**
     * Highlights a specific cell in the map with a temporary color change.
     * This is used to visually indicate actions such as successful moves or attacks.
     * @param row The row index of the cell to highlight
     * @param col The column index of the cell to highlight
     * @param color The color to use for highlighting
     */
    public void highlightCell(int row, int col, Color color) {
        JButton cell = cellButtons[row][col];
        Color original = cell.getBackground();
        cell.setBackground(color);
        new Timer(300, e -> cell.setBackground(original)).start();
    }

    /**
     * refreshes the map display when the game state is updated.
     */
    @Override
    public void onGameUpdated() {
        refresh();
    }

    // --- Fields ---
    private final JButton[][] cellButtons;
    private final GameController controller;
}



