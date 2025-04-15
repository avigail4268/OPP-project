package game.items;

import game.map.Position;

public class Wall extends GameItem {

    public Wall(Position position, boolean blocksMovement, String description) {
        super(position, blocksMovement, description);
    }

    @Override
    public String toString() {
        return "Wall";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wall) {
            Wall w = (Wall) obj;
            return this.getPosition().equals(w.getPosition());
        }
        return false;
    }

    @Override
    public String getDisplaySymbol() {
        return "W";
    }
}
