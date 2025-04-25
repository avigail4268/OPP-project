package game.items;
import game.core.GameEntity;
import game.map.Position;

public abstract class GameItem implements GameEntity {

    public GameItem(Position position, boolean blocksMovement) {
        this.position = position;
        this.blocksMovement = blocksMovement;
        this.description = getDescription();
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
    public boolean getVisible() {
        return visible;
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

    public String getDescription()
    {
        return "This is a game item.";
    }
    protected void setDescription(String description) {
         this.description = description;
    }

    @Override
    public abstract String toString();
    @Override
    public abstract boolean equals(Object obj);
    @Override
    public abstract String getDisplaySymbol();

    private Position position;
    private final boolean blocksMovement;
    private String description;
    private boolean visible;

}
