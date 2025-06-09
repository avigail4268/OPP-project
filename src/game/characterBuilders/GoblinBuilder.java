package game.characterBuilders;

import game.characters.Goblin;
import game.characters.Orc;
import game.map.Position;

import java.util.Random;

public class GoblinBuilder {
    private Goblin goblin;
    private final Random r = new Random();

    public GoblinBuilder() {
        this.goblin = new Goblin(new Position(0, 0), 50); // Default position and health
    }
}
