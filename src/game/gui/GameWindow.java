package game.gui;

import game.engine.GameWorld;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    public GameWindow(GameWorld game) {
        setTitle("Game Bord");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        MapPanel mapPanel = new MapPanel(game);
        StatusPanel statusPanel = new StatusPanel(game.getCurrentPlayer());

        add(mapPanel, BorderLayout.CENTER); // פה נמצא ה־GridLayout
        add(statusPanel, BorderLayout.SOUTH);

        setSize(800, 800);
        setVisible(true);
    }
}
