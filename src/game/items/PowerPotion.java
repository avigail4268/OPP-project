package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

public class PowerPotion extends Potion {
    public PowerPotion(Position position, boolean blocksMovement, String description,int max,int min) {
        super(position, blocksMovement, description,max,min);
    }
    public void interact(PlayerCharacter c) {
        if(!getIsUsed()){
            int amount = this.getIncreaseAmount();
            c.setPower(amount);
            this.setIsUsed();
            c.getInventory().removeItem(this);
            System.out.println("You power was " + (c.getPower()-amount) + " now is " + (c.getPower()));
        }
    }
    public void collect(PlayerCharacter c) {
        c.addToInventory(this);
        System.out.println("Added power potion to your inventory! ");
    }
    @Override
    public String getDisplaySymbol() {
        return "PP";
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PowerPotion) {
            PowerPotion p = (PowerPotion) obj;
            return this.getPosition().equals(p.getPosition()) && this.getIsUsed() == p.getIsUsed();
        }
        return false;
    }
    public String toString() {
        return "Power potion " + getPosition();

    }
}
