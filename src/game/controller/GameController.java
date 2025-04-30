package game.controller;

import javax.swing.*;

import game.characters.PlayerCharacter;
import game.engine.*;
import game.gui.CellButton;
import java.util.Objects;

public class GameController {
    private GameWorld game;

    public GameController(GameWorld game) {
        this.game = game;
    }

    public void handleLeftClick(int row, int col) {
        CellButton tile = game.getCell(row, col);

        if (tile.isWalkable()) {
            game.movePlayerTo(row, col);
        } else if (tile.hasEnemy()) {
            game.fightEnemyAt(row, col);
        } else if (tile.hasItem()) {
            game.pickUpItemAt(row, col);
        }
    }

    public void handleRightClick(int row, int col, JButton sourceButton) {
        CellButton tile = game.getTile(row, col);
        JPopupMenu popup = new JPopupMenu();


        if (tile.hasEnemy()) {
            popup.add(new JMenuItem("Enemy: " + tile.getEnemy().getName()));
            popup.add(new JMenuItem("HP: " + tile.getEnemy().getCurrentHP()));
        } else if (tile.hasItem()) {
            popup.add(new JMenuItem("Item: " + tile.getItem().getDescription()));
        } else if (tile.isWall()) {
            popup.add(new JMenuItem("Wall - impassable"));
        } else {
            popup.add(new JMenuItem("Empty tile"));
        }

        popup.show(sourceButton, sourceButton.getWidth() / 2, sourceButton.getHeight() / 2);
    }

    public ImageIcon getIconForTile(int row, int col) {
        CellButton tile = game.getTile(row, col);
        String path = "/resources/images/";

        if (!game.isVisibleToPlayer(row, col)) {
            path += "unknown.png";
        } else if (tile.hasPlayer()) {
            path += "player.png";
        } else if (tile.hasEnemy()) {
            path += "enemy.png";
        } else if (tile.hasItem()) {
            path += "item.png";
        } else if (tile.isWall()) {
            path += "wall.png";
        } else {
            path += "floor.png";
        }

        return new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
    }

    public int getMapRows() {
        return game.getMap().getSize();
    }

    public int getMapCols() {
        return game.getMap().getSize();
    }

    public int getPlayerHP() {
        return game.getCurrentPlayer().getHealth();
    }

    public int getPlayerMaxHP() {
        return 100;
    }
    public PlayerCharacter getPlayer() {
        return game.getCurrentPlayer();
    }
}