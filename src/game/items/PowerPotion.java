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
            this.
        }
    }

}
