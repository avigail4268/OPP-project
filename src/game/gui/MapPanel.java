//package game.gui;
//
//import game.engine.GameWorld;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class MapPanel extends JPanel {
//    public MapPanel(GameWorld game) {
//        int size = game.getMap().getSize();
//        setLayout(new GridLayout(size, size));
//
//        for (int i = 0; i < size*size; i++) {
//            JButton tile = new JButton();
//            add(tile);
//        }
//    }
//}
//

package game.gui;

import javax.swing.*;
import java.awt.*;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;

public class MapPanel extends JPanel {

    private GameWorld game;

    public MapPanel(GameWorld game) {
        this.game = game;
        int size = game.getMap().getSize(); // נניח ש־getSize() מחזיר את גודל המפה
        setLayout(new GridLayout(size, size)); // או גודל אחר של המפה

        buildMap();
    }

    private void buildMap() {
        removeAll(); // ניקוי ישן אם מרעננים

        // נניח שזו המפה שלך:
        var map = game.getMap(); // צריך לוודא ש־getMap() מחזיר Tile[][] או משהו דומה

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                JButton tileButton = new JButton();
                // כאן תבחרי איזה אייקון להציג לפי סוג התא:
                ImageIcon icon = getIconFor(map[i][j]);
                tileButton.setIcon(icon);

                // כאן נוסיף מאזין עכבר (נרחיב בהמשך)
                add(tileButton);
            }
        }
        revalidate();
        repaint();
    }

    private ImageIcon getIconFor(Object tileContent) {
        // מחזיר את האייקון המתאים לפי סוג האובייקט שבתא
        // דוגמה:
        if (tileContent instanceof PlayerCharacter) {
            return new ImageIcon("resources/images/player.png");
        } else if (tileContent instanceof Enemy) {
            return new ImageIcon("resources/images/enemy.png");
        } else if (tileContent instanceof Wall) {
            return new ImageIcon("resources/images/wall.png");
        } else {
            return new ImageIcon("resources/images/empty.png");
        }
    }
}