package game.controller;
import javax.swing.*;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.gui.GameFrame;
import game.items.GameItem;
import game.map.Position;
import game.core.GameEntity;
//import game.gui.SoundPlayer;
import java.awt.*;
import java.util.List;

public class GameController {
    public GameController(GameWorld engine) {
        this.engine = engine;
    }
    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }
    public void handleLeftClick(int row, int col) {
        Position clickedPos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(clickedPos);
        Position playerPos = engine.getPlayer().getPosition();
        if (engine.isValidMove(playerPos, clickedPos, engine.getPlayer())) {
            engine.movePlayerTo(clickedPos);
//            SoundPlayer.playSound("step.wav");
            frame.refresh();
        } else if (CellTypeDetector.hasEnemy(entities)) {
            engine.fightEnemyAt(clickedPos);
            if (engine.getPlayer().isDead()) {
                JOptionPane.showMessageDialog(frame, "GAME OVER!", "You Died", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
//            SoundPlayer.playSound("attack.wav");
            if (frame instanceof game.gui.GameFrame gf) {
                gf.getMapPanel().highlightCell(row, col, Color.RED);
                frame.refresh();
            }
            frame.refresh();
        } else if (CellTypeDetector.hasItem(entities)) {
            engine.pickUpItemAt(clickedPos);
//            SoundPlayer.playSound("pickup.wav");
            checkVictory();
            frame.refresh();
            if (frame instanceof game.gui.GameFrame gf) {
                gf.getMapPanel().highlightCell(row, col, Color.GREEN);
                frame.refresh();
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

        String path = "/images/";

        if (CellTypeDetector.hasPlayer(entities)) {
            PlayerCharacter player = CellTypeDetector.getFirstPlayer(entities);
            path += player.getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasEnemy(entities)) {
            Enemy enemy = CellTypeDetector.getFirstEnemy(entities);
            path += enemy.getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasItem(entities)) {
            GameItem item = CellTypeDetector.getFirstItem(entities);
            path += item.getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasWall(entities)) {
            path += "Wall.png";
        } else {
            return null;

        }
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            return null;
        }
    }
    private void checkVictory() {
        if (engine.getPlayer().getTreasurePoints() >= 500) {
            JOptionPane.showMessageDialog(frame, "You Win!", "You achieved more than 500 points!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    public int getMapRows() {
        return engine.getMap().getSize();
    }
    public int getMapCols() {
        return engine.getMap().getSize();
    }
    public PlayerCharacter getPlayer() {
        return engine.getPlayer();
    }

    private GameWorld engine;
    private GameFrame frame;
}
