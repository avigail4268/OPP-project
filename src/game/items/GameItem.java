package game.items;
import game.core.GameEntity;
import game.map.Position;

public abstract class GameItem implements GameEntity {
    private Position position;
    private boolean blocksMovement;
    private String description;

    public GameItem(Position position, boolean blocksMovement, String description) {
        this.position = position;
        this.blocksMovement = blocksMovement;
        this.description = description;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public abstract String getDisplaySymbol();

    @Override
    public boolean setVisible(boolean visible) {
        return false;
    }

    @Override
    public boolean setPosition(Position newPos) {
        return false;
    }

    public abstract boolean equals(Object obj);
    public abstract String toString();


}
