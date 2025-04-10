package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

public class Potion extends GameItem implements Interactable {

    private int increaseAmount = new Random().nextInt(10, 50);
    boolean isUsed = false;

    public Potion(Position position, boolean blocksMovement, String description) {
        super(position, blocksMovement, description);
    }

    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed) {
            // TODO amount should be a max between increaseAmount and completion to player health
            int amount = increaseAmount;
            c.heal(amount);
        }
    }
}
