package game.gui;

import game.engine.GameWorld;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    public MapPanel(GameWorld game) {
        int size = game.getMap().getSize();
        setLayout(new GridLayout(size, size));

        for (int i = 0; i < size*size; i++) {
            JButton tile = new JButton();
            add(tile);
        }
    }
}
