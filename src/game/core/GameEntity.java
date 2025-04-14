package game.core;

import game.map.Position;

public interface GameEntity {
    Position getPosition();
    boolean setPosition(Position newPos);
    String getDisplaySymbol();
    void setVisible(boolean visible);
}
