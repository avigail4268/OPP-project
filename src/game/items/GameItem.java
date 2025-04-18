package game.items;
import game.core.GameEntity;
import game.map.Position;

public abstract class GameItem implements GameEntity {
    private Position position;
    private boolean blocksMovement;
    private String description;
    private boolean visible;

    public GameItem(Position position, boolean blocksMovement, String description) {
        this.position = position;
        this.blocksMovement = blocksMovement;
        this.description = description;
        this.visible = false;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    @Override
    public boolean setPosition(Position newPos) {
        if (newPos != null) {
            this.position = newPos;
            return true;
        }
        return false;
    }
    public boolean isBlocksMovement() {
        return blocksMovement;
    }

//    public String getDescription() {
//        return description;
//    }

    @Override
    public abstract String toString();

    public abstract boolean equals(Object obj);

    @Override
    public abstract String getDisplaySymbol();
}
