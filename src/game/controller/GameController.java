package game.controller;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import game.audio.SoundPlayer;
import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.gui.GameFrame;
import game.gui.GameSetUp;
import game.items.GameItem;
import game.log.LogManager;
import game.map.Position;
import game.core.GameEntity;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controls game interactions between user input, game logic, and GUI.
 * Acts as the bridge between the GameWorld engine and the GameFrame.
 */
public class GameController {

    /**
     * Constructs a GameController for the given GameWorld engine.
     * @param engine the GameWorld instance used to manage the game logic.
     */
    public GameController(GameWorld engine) {
        this.engine = engine;
    }

    /**
     * Sets the GameFrame used by this controller.
     * @param frame the main game window frame.
     */
    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }

    /**
     * Sets the size of tiles in pixels.
     * @param tileSize the tile size.
     */
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Gets the number of rows in the map.
     * @return map size (rows).
     */
    public int getMapRows() {
        return engine.getMap().getSize();
    }

    /**
     * Gets the number of columns in the map.
     * @return map size (columns).
     */
    public int getMapCols() {
        return engine.getMap().getSize();
    }

    /**
     * Returns the player character.
     * @return the PlayerCharacter instance.
     */
    public PlayerCharacter getPlayer() {
        return engine.getPlayer();
    }

    /**
     * Alias for getEngine().
     * @return the GameWorld instance.
     */
    public GameWorld getGameWorld() {
        return engine;
    }

    /**
     * Handles a left-click at a specific tile.
     * Moves the player, triggers combat, or picks up items.
     * @param row the row clicked.
     * @param col the column clicked.
     */
    public void handleLeftClick(int row, int col) {

        Position clickedPos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(clickedPos);
        Position playerPos = engine.getPlayer().getPosition();


        engine.getPlayer().update(); // Update health regeneration if applicable


        if (engine.isValidMove(playerPos, clickedPos)) {
            ReentrantLock lock = GameWorld.getMapLock(clickedPos);
            boolean acquired = false;
            try {
                acquired = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (acquired) {
                    if (engine.getMap().isEmpty(clickedPos)) {
                        engine.movePlayerTo(clickedPos);
                        SoundPlayer.playSound("footsteps.wav");
                        engine.notifyObservers();
                    } else if (CellTypeDetector.hasEnemy(entities)) {
                        engine.fightEnemyAt(clickedPos);
                        if (engine.getPlayer().isDead()) gameOver();
                        SoundPlayer.playSound("classic_attack.wav");
                        engine.notifyObservers();
                    } else if (CellTypeDetector.hasItem(entities)) {
                        engine.pickUpItemAt(clickedPos);
                        SoundPlayer.playSound("item_pickup.wav");
                        checkVictory();
                        if (frame instanceof GameFrame gf) {
                            gf.getMapPanel().highlightCell(row, col, Color.GREEN);
                        }
                        engine.movePlayerTo(clickedPos);
                        engine.notifyObservers();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (acquired) lock.unlock();
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
        } else if (CellTypeDetector.hasPlayer(entities)) {
            popup.add(new JMenuItem("Player: " + getPlayer().toString()));
        } else {
            popup.add(new JMenuItem("Empty tile"));
        }

        popup.show(sourceButton, sourceButton.getWidth() / 2, sourceButton.getHeight() / 2);
    }

    /**
     * Moves the player using arrow key direction input.
     * @param direction one of "UP", "DOWN", "LEFT", "RIGHT".
     */
    public void handleArrowKey(String direction) {
        int currentRow = getPlayer().getPosition().getRow();
        int currentCol = getPlayer().getPosition().getCol();

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

        if (newRow >= 0 && newRow < getMapRows() && newCol >= 0 && newCol < getMapCols()) {
            handleLeftClick(newRow, newCol);
        }
    }

    /**
     * Generates the image for a tile based on its contents.
     * @param row tile row.
     * @param col tile column.
     * @return icon representing the tile.
     */
    public ImageIcon getIconForTile(int row, int col) {
        Position pos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(pos);

        if (entities == null || !engine.isVisibleToPlayer(row, col)) return null;

        String path = "/images/";
        if (CellTypeDetector.hasPlayer(entities)) {
            path += CellTypeDetector.getFirstPlayer(entities).getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasEnemy(entities)) {
            path += CellTypeDetector.getFirstEnemy(entities).getDisplaySymbol() + ".png";
        } else if (CellTypeDetector.hasItem(entities)) {
            path += CellTypeDetector.getFirstItem(entities).getDisplaySymbol() + ".png";
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
        }
        return null;
    }

    /**
     * Generates a tile image with a health bar overlay.
     * @param row tile row.
     * @param col tile column.
     * @return icon with health bar overlay.
     */
    public ImageIcon getIconWithHealthBar(int row, int col) {
        Position pos = new Position(row, col);
        List<GameEntity> entities = engine.getMap().getEntitiesAt(pos);

        if (entities == null || !engine.isVisibleToPlayer(row, col)) return null;

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
            int health = -1, maxHealth = -1;
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
                g.fillRect(0, 0, (int) (width * percent), healthBarHeight);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, width - 1, healthBarHeight - 1);
                break;
            }
        }
        g.dispose();
        return new ImageIcon(imageWithBar);
    }

    /**
     * Gets the current game setup instance.
     * @return the GameSetUp instance.
     */
    public GameSetUp getSetUp() {
        return setUp;
    }

    /**
     * Refreshes the game frame to update the GUI.
     */
    public void refresh(){
        frame.refresh();
    }

    /**
     * Checks victory condition (500 treasure points).
     * Displays victory screen and shuts down game.
     */
    private void checkVictory() {
        if (engine.getPlayer().getTreasurePoints() >= 500) {
            LogManager.addLog("Game ended, player achieve 500 pt and won the game!");
            SoundPlayer.playSound("winner.wav");
            engine.shutdown();
            LogManager.stop();
            engine.getIsGameRunning().set(false);

            setUp.showVictoryPanel(() -> {
                System.exit(0);
            });
        }
    }

    /**
     * Displays a game over screen and ends the game if the player dies.
     */
    private void gameOver() {
        LogManager.addLog("Game ended, player died!");
        SoundPlayer.playSound("losing.wav");
        engine.shutdown();
        LogManager.stop();
        engine.getIsGameRunning().set(false);

        setUp.gameOverPanel(() -> {
            System.exit(0);
        });

    }


// --- Fields ---

private int tileSize = 64;
private GameWorld engine;
private GameFrame frame;
private GameSetUp setUp = new GameSetUp();


}

