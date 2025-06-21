package game.gui;

import game.audio.SoundPlayer;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.gameSaver.GameCaretaker;
import game.gameSaver.GameMemento;
import game.items.GameItem;
import game.observer.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The StatusPanel class displays the player's status, including health, inventory,
 * and other attributes. It allows the player to save and load the game state.
 */
public class StatusPanel extends JPanel implements GameObserver {

    /**
     * Constructs a StatusPanel with the given player character and game world.
     * Initializes the layout, labels, health bar, and inventory display.
     * @param player The player character whose status is displayed
     * @param world The game world context for saving/loading game state
     */
    public StatusPanel(PlayerCharacter player , GameWorld world) {
        this.player = player;
        this.world = world;

        // Set layout and basic properties
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBorder(BorderFactory.createTitledBorder("Player Status"));

        // Create labels for player attributes
        nameLabel = new JLabel("Name: " + player.getName());
        classLabel = new JLabel("Type: " + player.getDisplaySymbol());
        treasureLabel = new JLabel("Treasure Points: " + player.getTreasurePoints());
        powerPanel = new JLabel("Power: " + player.getPower());

        // Create and configure health bar
        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(player.getHealth());
        healthBar.setStringPainted(true);
        updateHealthBarColor(player.getHealth());

        // Top panel contains all main info
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(nameLabel);
        topPanel.add(classLabel);
        topPanel.add(treasureLabel);
        topPanel.add(powerPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(healthBar);

        // Inventory panel setup
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        inventoryPanel.setBackground(new Color(245, 245, 245));

        // Scroll pane for inventory
        inventoryScroll = new JScrollPane(inventoryPanel);
        inventoryScroll.setPreferredSize(new Dimension(220, 150));
        inventoryScroll.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add components to main layout
        add(topPanel, BorderLayout.NORTH);
        add(inventoryScroll, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        save = new JButton("Save");
        load= new JButton("Load");

        buttonsPanel.add(save);
        buttonsPanel.add(load);
        // Add buttons panel to the bottom
        add(buttonsPanel, BorderLayout.SOUTH);
        save.addActionListener(e -> {
            GameCaretaker.getInstance().save(world);
            JOptionPane.showMessageDialog(this, "Game saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
        });

        load.addActionListener(e -> {
           GameMemento memento = GameCaretaker.getInstance().load();
            if (memento != null) {
                world.restoreFromMemento(memento);
                JOptionPane.showMessageDialog(this, "Game loaded!", "Load", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No saved game found.", "Load", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /**
     * Refreshes the status panel with the updated player data.
     * @param player The player character whose data should be refreshed
     */
    public void refresh(PlayerCharacter player) {
        this.player = player;

        nameLabel.setText("Name: " + player.getName());
        classLabel.setText("Type: " + player.getDisplaySymbol());
        treasureLabel.setText("Treasure Points: " + player.getTreasurePoints());
        powerPanel.setText("Power: " + player.getPower());
        healthBar.setValue(player.getHealth());
        updateHealthBarColor(player.getHealth());
        updateInventory(player.getInventory().getItems());
    }

    /**
     * Called when the game state is updated. Refreshes the panel.
     */
    @Override
    public void onGameUpdated() {
        refresh(player);
    }

    /**
     * Updates the inventory display with the given list of items.
     * @param items List of GameItem objects to display
     */
    private void updateInventory(List<GameItem> items) {
        inventoryPanel.removeAll();

        for (GameItem item : items) {
            JButton itemButton = new JButton(item.getDisplaySymbol());
            itemButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Add behavior when item is clicked
            itemButton.addActionListener(e -> {
                boolean used = player.useItem(item);
                SoundPlayer.playSound("use_potion.wav");
                if (used) {
                    refresh(player);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot use this item!", "Item Use", JOptionPane.WARNING_MESSAGE);
                }
            });

            inventoryPanel.add(itemButton);
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    /**
     * Updates the health bar color based on the current health value.
     * @param current The player's current health (0–100)
     */
    private void updateHealthBarColor(int current) {
        double percent = (double) current / 100;
        if (percent > 0.7) {
            healthBar.setForeground(Color.GREEN);
        } else if (percent > 0.3) {
            healthBar.setForeground(Color.ORANGE);
        } else {
            healthBar.setForeground(Color.RED);
        }
    }

    // --- Fields ---
    private JLabel nameLabel;
    private JLabel classLabel;
    private JLabel treasureLabel;
    private JProgressBar healthBar;
    private JPanel inventoryPanel;
    private JLabel powerPanel;
    private JScrollPane inventoryScroll;
    private PlayerCharacter player;
    private GameWorld world;
    private JButton save;
    private JButton load ;

}



