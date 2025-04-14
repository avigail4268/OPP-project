package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;
import java.util.Random;

public class Potion extends GameItem implements Interactable {

    private int increaseAmount;
    private boolean isUsed;

    public Potion(Position position, boolean blocksMovement, String description) {
        super(position, blocksMovement, description);
        this.increaseAmount = new Random().nextInt(10, 50);
        this.isUsed = false;
    }

    @Override
    public String getDisplaySymbol() {
        return "P";
    }

    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed) {
            int amount = Math.min(this.increaseAmount, 100 - c.getHealth());
            c.heal(amount);
            this.isUsed = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Potion) {
            Potion potion = (Potion) obj;
            return isUsed == potion.isUsed && getPosition().equals(potion.getPosition());
        }
        return false;
    }
}
