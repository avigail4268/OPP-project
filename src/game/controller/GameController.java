package game.controller;
import javax.swing.*;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.items.GameItem;
import game.map.Position;
import game.core.GameEntity;
//import game.gui.SoundPlayer;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class GameController {
    private GameWorld engine;
    private JFrame frame;

    public GameController(GameWorld engine) {
        this.engine = engine;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void handleLeftClick(int row, int col) {
        Position clickedPos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(clickedPos);
        Position playerPos = engine.getPlayer().getPosition();

        if (engine.isValidMove(playerPos, clickedPos, engine.getPlayer())) {
            engine.movePlayerTo(clickedPos);
//            SoundPlayer.playSound("step.wav");
        } else if (CellTypeDetector.hasEnemy(entities)) {
            engine.fightEnemyAt(clickedPos);
//            SoundPlayer.playSound("attack.wav");
            if (frame instanceof game.gui.GameFrame gf) {
                gf.getMapPanel().highlightCell(row, col, Color.RED);
            }
        } else if (CellTypeDetector.hasItem(entities)) {
            engine.pickUpItemAt(clickedPos);
//            SoundPlayer.playSound("pickup.wav");
            if (frame instanceof game.gui.GameFrame gf) {
                gf.getMapPanel().highlightCell(row, col, Color.GREEN);
            }
        }
    }

    public void handleRightClick(int row, int col, JButton sourceButton) {
        Position pos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(pos);
        JPopupMenu popup = new JPopupMenu();

        if (CellTypeDetector.hasEnemy(entities)) {
            Enemy e = CellTypeDetector.getFirstEnemy(entities);
            JPanel infoPanel = new JPanel(new GridLayout(0, 1));
            infoPanel.add(new JLabel("Enemy: " + e.getDisplaySymbol()));
            infoPanel.add(new JLabel("HP: " + e.getHealth()));
            popup.add(infoPanel);
        } else if (CellTypeDetector.hasItem(entities)) {
            GameItem item = CellTypeDetector.getFirstItem(entities);
            popup.add(new JMenuItem("Item: " + item.getDescription()));
        } else if (CellTypeDetector.hasWall(entities)) {
            popup.add(new JMenuItem("Wall - impassable"));
        } else {
            popup.add(new JMenuItem("Empty tile"));
        }

        popup.show(sourceButton, sourceButton.getWidth() / 2, sourceButton.getHeight() / 2);
    }

    public ImageIcon getIconForTile(int row, int col) {
        Position pos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(pos);

        if (entities == null || !engine.isVisibleToPlayer(row, col)) {
            return null;
        }

        String path = "/resources/images/";

        if (CellTypeDetector.hasPlayer(entities)) {
            PlayerCharacter player = CellTypeDetector.getFirstPlayer(entities);
            path += player.getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasEnemy(entities)) {
            Enemy enemy = CellTypeDetector.getFirstEnemy(entities);
            path += enemy.getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasItem(entities)) {
            GameItem item = CellTypeDetector.getFirstItem(entities);
            path += item.getDescription() + ".png";
        } else if (CellTypeDetector.hasWall(entities)) {
            path += "Wall.png";
        } else {
            return null;

        }


    public int getMapRows() {
        return engine.getMap().getSize();
    }

    public int getMapCols() {
        return engine.getMap().getSize();
    }

    public int getPlayerHP() {
        return engine.getPlayer().getHealth();
    }

    public int getPlayerMaxHP() {
        return 100;
    }

//    public int getPlayerPotionCount() {
//        return engine.getPlayer().getInventory().countPotions();
//    }

    public PlayerCharacter getPlayer() {
        return engine.getPlayer();
    }
}
