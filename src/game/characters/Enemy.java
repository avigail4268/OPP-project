package game.characters;

import game.map.Position;

public abstract class Enemy extends AbstractCharacter {
    private boolean visible;

    public Enemy(Position position) {
        super(position);
    }
}
