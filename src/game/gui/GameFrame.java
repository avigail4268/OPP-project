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

        mapPanel = new MapPanel(controller);
        statusPanel = new StatusPanel(controller.getPlayer());

        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refresh() {
        mapPanel.refresh();
        statusPanel.refresh(controller.getPlayer());
    }
}