package game.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import game.controller.GameController;

/**
 * The GameFrame class represents the main window (JFrame) of the game GUI.
 * It contains the map panel (center) and the status panel (east),
 * and initializes the layout and observers.
 */
public class GameFrame extends JFrame {

    /**
     * Constructs the game frame with the given game controller.
     * Initializes the layout, adds the panels, and registers observers.
     * @param controller The game controller that manages the game state and player
     */
    public GameFrame(GameController controller) {
        this.controller = controller;

        setTitle("Dungeons & Dragons Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog (
                        GameFrame.this,
                        "Are you sure you want to exit the game?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                   controller.getSetUp().exitGame(controller.getGameWorld()); // Call the controller's exit method to handle game exit logic
                }
            }
        });

        // Set layout manager to BorderLayout
        setLayout(new BorderLayout());

        // Create and add the map panel (center) and status panel (east)
        mapPanel = new MapPanel(controller);
        statusPanel = new StatusPanel(controller.getPlayer(), controller.getGameWorld());

        // Register the panels as observers to the controller
        controller.getGameWorld().addObserver(mapPanel);
        controller.getGameWorld().addObserver(statusPanel);

        // Add panels to the frame
        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.EAST);

        // Adjust frame size to fit content
        pack();

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Make the frame visible
        setVisible(true);
    }

    /**
     * Refreshes the display of both the map panel and the status panel.
     * Called when the game state changes and the GUI needs to update.
     */
    public void refresh() {
        mapPanel.refresh();
        statusPanel.refresh(controller.getPlayer());
    }

    /**
     * Returns the map panel component.
     * @return The MapPanel instance used in the GUI
     */
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    // --- Fields ---
    private MapPanel mapPanel;
    private StatusPanel statusPanel;
    private GameController controller;
}


