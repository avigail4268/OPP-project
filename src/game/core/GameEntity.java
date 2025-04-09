package game.core;

public interface GameEntity {
    Position getPosition();
    boolean setPosition(Position newPos);
    String getDisplaySymbol();
    boolean setVisible(boolean visible);
}
