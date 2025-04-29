//package game.gui;
//
//import game.engine.GameWorld;
//import javax.swing.*;
//import java.awt.*;
//
//public class GameFrame extends JFrame {
//    public GameFrame(GameWorld game) {
//        setTitle("Game Bord");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        MapPanel mapPanel = new MapPanel(game);
//        StatusPanel statusPanel = new StatusPanel(game.getCurrentPlayer());
//
//        add(mapPanel, BorderLayout.CENTER);
//        add(statusPanel, BorderLayout.SOUTH);
//
//        setSize(800, 800);
//        setVisible(true);
//    }
//
//}

package game.gui;

import javax.swing.*;
import java.awt.*;
import game.controller.GameController;

public class GameFrame extends JFrame {
    private MapPanel mapPanel;
    private StatusPanel statusPanel;
    private GameController controller;

    public GameFrame(GameController controller) {
        this.controller = controller;
        setTitle("Dungeons & Dragons Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // יצירת רכיבי GUI
        mapPanel = new MapPanel(controller);
        statusPanel = new StatusPanel(controller);

        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // מרכז המסך
        setVisible(true);
    }

    public void refresh() {
        mapPanel.refresh();
        statusPanel.refresh();
    }
}