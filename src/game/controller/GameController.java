package game.controller;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import game.audio.SoundPlayer;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.gui.GameFrame;
import game.items.GameItem;
import game.map.Position;
import game.core.GameEntity;
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
            if (engine.getMap().isEmpty(clickedPos)) {
                engine.movePlayerTo(clickedPos);
                SoundPlayer.playSound("footsteps.wav");
                frame.refresh();
            } else if (CellTypeDetector.hasEnemy(entities)) {
                engine.fightEnemyAt(clickedPos);
                if (engine.getPlayer().isDead()) {
                    JOptionPane.showMessageDialog(frame, "GAME OVER!", "You Died", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                SoundPlayer.playSound("classic_attack.wav");
                if (frame instanceof game.gui.GameFrame gf) {
                    gf.getMapPanel().highlightCell(row, col, Color.RED);
                }
                frame.refresh();
            } else if (CellTypeDetector.hasItem(entities)) {
                engine.pickUpItemAt(clickedPos);
                SoundPlayer.playSound("item_pickup.wav");
                checkVictory();
                if (frame instanceof game.gui.GameFrame gf) {
                    gf.getMapPanel().highlightCell(row, col, Color.GREEN);
                }
                engine.movePlayerTo(clickedPos);
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
            popup.add(new JMenuItem("Wall - impassable to pass"));
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
            Image scaledImage = originalIcon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            return null;
        }
    }

    public ImageIcon getIconWithHealthBar(int row, int col) {
        Position pos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(pos);

        if (entities == null || !engine.isVisibleToPlayer(row, col)) {
            return null;
        }

        ImageIcon baseIcon = getIconForTile(row, col);
        if (baseIcon == null) return null;

        Image baseImage = baseIcon.getImage();
        int width = baseImage.getWidth(null);

        int healthBarHeight = 4;
        int iconYOffset = 3;

        BufferedImage imageWithBar = new BufferedImage(tileSize, tileSize + iconYOffset, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithBar.createGraphics();

        g.drawImage(baseImage, 0, iconYOffset, tileSize, tileSize, null);

        for (GameEntity entity : entities) {
            int health = -1;
            int maxHealth = -1;

            if (entity instanceof PlayerCharacter p) {
                health = p.getHealth();
                maxHealth = p.getMaxHealth();
            } else if (entity instanceof Enemy e) {
                health = e.getHealth();
                maxHealth = e.getMaxHealth();
            }

            if (health >= 0) {
                double percent = (double) health / maxHealth;
                Color barColor = percent > 0.7 ? Color.GREEN : (percent > 0.3 ? Color.ORANGE : Color.RED);

                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, width, healthBarHeight);

                g.setColor(barColor);
                g.fillRect(0, 0, (int)(width * percent), healthBarHeight);

                g.setColor(Color.BLACK);
                g.drawRect(0, 0, width - 1, healthBarHeight - 1);

                break;
            }
        }

        g.dispose();
        return new ImageIcon(imageWithBar);
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
    public GameWorld getEngine() {
        return engine;
    }
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    public void handleArrowKey(String direction) {
        int currentRow = getPlayer().getPosition().getRow(); // נניח שיש getRow()
        int currentCol = getPlayer().getPosition().getCol(); // נניח שיש getCol()

        int newRow = currentRow;
        int newCol = currentCol;

        switch (direction) {
            case "UP":
                newRow--;
                break;
            case "DOWN":
                newRow++;
                break;
            case "LEFT":
                newCol--;
                break;
            case "RIGHT":
                newCol++;
                break;
        }

        // בדיקה שהמיקום החדש בתוך גבולות המפה
        if (newRow >= 0 && newRow < getMapRows() && newCol >= 0 && newCol < getMapCols()) {
            engine.movePlayerTo(new Position(newRow,newCol)); // נניח שיש moveTo(row, col)
            frame.refresh(); // רענון המסך (או דרך אחרת להציג עדכון)
        }
    }

    private int tileSize = 64;
    private GameWorld engine;
    private GameFrame frame;
}







