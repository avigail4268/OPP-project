package game.characters;

import game.map.Position;

public class Warrior extends PlayerCharacter {

    public Warrior(String playerName, Position position) {
        super(playerName, position);
    }

    @Override
    public String getDisplaySymbol() {
        return "W";
    }

    @Override
    public boolean setVisible(boolean visible) {
        return false;
    }
}
