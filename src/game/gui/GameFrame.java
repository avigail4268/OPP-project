package game.gui;
import javax.swing.*;
import java.awt.*;
import game.controller.GameController;

public class GameFrame extends JFrame {
    public GameFrame(GameController controller) {
        this.controller = controller;
        setTitle("Dungeons & Dragons Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapPanel = new MapPanel(controller);
        statusPanel = new StatusPanel(controller.getPlayer());

        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void refresh() {
        mapPanel.refresh();
        statusPanel.refresh(controller.getPlayer());
    }
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    private MapPanel mapPanel;
    private StatusPanel statusPanel;
    private GameController controller;

}