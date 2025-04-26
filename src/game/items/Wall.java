package game.items;
import game.map.Position;

public class Wall extends GameItem {

    public Wall(Position position, boolean blocksMovement) {
        super(position, blocksMovement);
        this.setDescription("This is a Wall, you can't pass it.");
    }
    @Override
    public String toString() {
        return "Wall at position " + getPosition();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wall other) {
            return this.getPosition().equals(other.getPosition());
        }
        return false;
    }
    @Override
    public String getDisplaySymbol() {
        return "WALL";
    }
    @Override
    public String getColorCode() {
        return "\u001B[37m"; // White for walls
    }
}
