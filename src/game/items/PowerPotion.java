package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

public class PowerPotion extends Potion {
    public PowerPotion(Position position, boolean blocksMovement, int max,int min) {
        super(position, blocksMovement,max,min);
        this.setDescription("This is power potion, increase power by " + this.getIncreaseAmount());
    }
    @Override
    public void interact(PlayerCharacter c) {
        if(!getIsUsed()){
            int amount = this.getIncreaseAmount();
            c.setPower(amount);
            if(this.setIsUsed()) {
                if (c.getInventory().removeItem(this)) {
                    System.out.println("Your power was " + (c.getPower()-amount) + " now is " + (c.getPower()));

                }
            }
        }
    }
    @Override
    public void collect(PlayerCharacter c) {
        if (c.addToInventory(this)) {
            System.out.println("Power Potion added to the inventory! ");
        }
    }
    @Override
    public String getDisplaySymbol() {
        return "PP";
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PowerPotion other) {
            return this.getPosition().equals(other.getPosition()) && this.getIsUsed() == other.getIsUsed();
        }
        return false;
    }
    @Override
    public String toString() {
        return "Power Potion " + getPosition();

    }
    @Override
    public boolean isUsableInUsePotion() {
        return false;
    }
}
