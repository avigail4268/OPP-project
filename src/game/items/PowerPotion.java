package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

public class PowerPotion extends Potion {
    public PowerPotion(Position position, boolean blocksMovement, String description,int max,int min) {
        super(position, blocksMovement, description,max,min);
    }
    public void interact(PlayerCharacter c) {
        if(!getIsUsed()){
            c.setPower(this.getIncreaseAmount());
            this.setIsUsed();
        }
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
