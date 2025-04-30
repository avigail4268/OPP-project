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
        updateHealthBarColor(player.getHealth());

        add(nameLabel);
        add(classLabel);
        add(healthLabel);
        add(healthBar);
    }

public void refresh(PlayerCharacter player) {
    nameLabel.setText("Player Name: " + player.getName());
    classLabel.setText("Player Type: " + player.getDisplaySymbol());
    healthLabel.setText("Health: " + player.getHealth() + "/" + 100);
    healthBar.setMaximum(100);
    healthBar.setValue(player.getHealth());
    updateHealthBarColor(player.getHealth());
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
}
