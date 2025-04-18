package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;
import java.util.Random;

public class Potion extends GameItem implements Interactable {

    private int increaseAmount;
    private boolean isUsed;

    public Potion(Position position, boolean blocksMovement, String description,int max,int min) {
        //when its health potion max =50 min = 10, when its power potion max = 5 min = 1
        super(position, blocksMovement, description);
        this.increaseAmount = new Random().nextInt(min, max);
        this.isUsed = false;
    }

    @Override
    public String getDisplaySymbol() {
        return "P";
    }

    protected boolean getIsUsed() {
        return isUsed;
    }
    protected boolean setIsUsed() {
        if(!isUsed){
            this.isUsed = true;
            return true;
        }
        return false;
    }
    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed) {
            int amount = Math.min(this.increaseAmount, 100 - c.getHealth());
            c.heal(amount);
            setIsUsed();
        }
    }
    protected int getIncreaseAmount() {
        return increaseAmount;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Potion) {
            Potion potion = (Potion) obj;
            return isUsed == potion.isUsed && getPosition().equals(potion.getPosition());
        }
        return false;
    }
    public String toString() {
        if (isUsed) {
            return "health potion already used!";
        }
        else {
            return "health potion not yet used!";
        }
    }
}
