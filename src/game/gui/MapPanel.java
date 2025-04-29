
package game.gui;

//import javax.swing.*;
//import java.awt.*;
//
//import game.characters.Enemy;
//import game.characters.PlayerCharacter;
//import game.engine.GameWorld;
//
//public class MapPanel extends JPanel {
//
//    private GameWorld game;
//
//    public MapPanel(GameWorld game) {
//        this.game = game;
//        int size = game.getMap().getSize(); // נניח ש־getSize() מחזיר את גודל המפה
//        setLayout(new GridLayout(size, size)); // או גודל אחר של המפה
//
//        buildMap();
//    }
//
//    private void buildMap() {
//        removeAll(); // ניקוי ישן אם מרעננים
//
//        // נניח שזו המפה שלך:
//        var map = game.getMap(); // צריך לוודא ש־getMap() מחזיר Tile[][] או משהו דומה
//
//        for (int i = 0; i < map.getSize(); i++) {
//            for (int j = 0; j < map.getSize(); j++) {
//                JButton tileButton = new JButton();
//                // כאן תבחרי איזה אייקון להציג לפי סוג התא:
//                ImageIcon icon = getIconFor(map[i][j]);
//                tileButton.setIcon(icon);
//
//                // כאן נוסיף מאזין עכבר (נרחיב בהמשך)
//                add(tileButton);
//            }
//        }
//        revalidate();
//        repaint();
//    }
//
//    private ImageIcon getIconFor(Object tileContent) {
//        // מחזיר את האייקון המתאים לפי סוג האובייקט שבתא
//        // דוגמה:
//        if (tileContent instanceof PlayerCharacter) {
//            return new ImageIcon("resources/images/player.png");
//        } else if (tileContent instanceof Enemy) {
//            return new ImageIcon("resources/images/enemy.png");
//        } else if (tileContent instanceof Wall) {
//            return new ImageIcon("resources/images/wall.png");
//        } else {
//            return new ImageIcon("resources/images/empty.png");
//        }
//    }
//}


import game.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// MapPanel מציג את המפה במשחק באמצעות GridLayout
class MapPanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private GameController controller;
    private JButton[][] grid;
    private int rows; // אפשר להתאים לפי גודל המפה שלך
    private int cols;

    public MapPanel(GameController controller) {
        this.controller = controller;
        this.rows = controller.getMapRows();
        this.cols = controller.getMapCols();


        setLayout(new GridLayout(rows, cols));
        grid = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                cell.setFocusPainted(false);

                final int r = row;
                final int c = col;

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            controller.handleLeftClick(r, c);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            controller.handleRightClick(r, c, cell);
                        }
                    }
                });

                grid[row][col] = cell;
                add(cell);
            }
        }
    }

    // פעולה לרענון התצוגה לפי מצב המודל
    public void refresh() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // כאן נטען את האייקון המתאים לפי מה שיש במודל
                ImageIcon icon = controller.getIconForTile(row, col);
                grid[row][col].setIcon(icon);
            }
        }
        revalidate();
        repaint();
    }
}