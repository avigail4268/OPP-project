package game.gui;

import game.characters.PlayerCharacter;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    private JLabel nameLabel;
    private JLabel classLabel;
    private JLabel healthLabel;
    private JProgressBar healthBar;

    public StatusPanel (PlayerCharacter player) {
        setLayout(new GridLayout(4,1));

        nameLabel = new JLabel("Player Name: " + player.getName());
        classLabel = new JLabel("Player Type: " + player.getDisplaySymbol());
        healthLabel = new JLabel("Health: " + player.getHealth() + "/" + 100);

        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(player.getHealth());
        healthBar.setStringPainted(true);
        updateHealthBarColor(player.getHealth(), 100);

        add(nameLabel);
        add(classLabel);
        add(healthLabel);
        add(healthBar);
    }

public void update(PlayerCharacter player) {
    nameLabel.setText("Player Name: " + player.getName());
    classLabel.setText("Player Type: " + player.getDisplaySymbol());
    healthLabel.setText("Health: " + player.getHealth() + "/" + 100);

    healthBar.setMaximum(100);
    healthBar.setValue(player.getHealth());
    updateHealthBarColor(player.getHealth(), 100);
    }

    private void updateHealthBarColor(int current, int max) {
        double percent = (double) current / max;

        if (percent > 0.7) {
            healthBar.setForeground(Color.GREEN);
        } else if (percent > 0.3) {
            healthBar.setForeground(Color.ORANGE);
        } else {
            healthBar.setForeground(Color.RED);
        }
    }
}
