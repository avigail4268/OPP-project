package game.gui;
import game.audio.SoundPlayer;
import game.characters.PlayerCharacter;
import game.items.GameItem;
import game.observer.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusPanel extends JPanel implements GameObserver {
    public StatusPanel(PlayerCharacter player) {
        this.player = player;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBorder(BorderFactory.createTitledBorder("Player Status"));

        nameLabel = new JLabel("Name: " + player.getName());
        classLabel = new JLabel("Type: " + player.getDisplaySymbol());
        treasureLabel = new JLabel("Treasure Points: " + player.getTreasurePoints());
        powerPanel = new JLabel("Power: " + player.getPower());

        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(player.getHealth());
        healthBar.setStringPainted(true);
        updateHealthBarColor(player.getHealth());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(nameLabel);
        topPanel.add(classLabel);
        topPanel.add(treasureLabel);
        topPanel.add(powerPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(healthBar);

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        inventoryPanel.setBackground(new Color(245, 245, 245));

        inventoryScroll = new JScrollPane(inventoryPanel);
        inventoryScroll.setPreferredSize(new Dimension(220, 150));
        inventoryScroll.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(topPanel, BorderLayout.NORTH);
        add(inventoryScroll, BorderLayout.CENTER);
    }
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
    private void updateInventory(List<GameItem> items) {
        inventoryPanel.removeAll();

        for (GameItem item : items) {
            JButton itemButton = new JButton(item.getDisplaySymbol());
            itemButton.setAlignmentX(Component.LEFT_ALIGNMENT);

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
    @Override
    public void GameUpdated() {
        refresh(player);
    }
    private JLabel nameLabel;
    private JLabel classLabel;
    private JLabel treasureLabel;
    private JProgressBar healthBar;
    private JPanel inventoryPanel;
    private JLabel powerPanel;
    private JScrollPane inventoryScroll;
    private PlayerCharacter player;
}
