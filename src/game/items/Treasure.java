package game.items;


import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

public class Treasure extends GameItem implements Interactable {
    private int value;
    private boolean collected;

    public Treasure(Position position, boolean blocksMovement, String description) {
        super(position,blocksMovement,description);
        Random rand = new Random();
        value = rand.nextInt(100,300);
        collected = false;
    }

    @Override
    public String getDisplaySymbol() {
        return "T";
    }

    public void interact(PlayerCharacter c) {
        double random = Math.random();
        String description;
        if (random <= 1.0/3.0) {
            description = "Health potion";
            Potion potion = new Potion(this.getPosition(),false,description,50,10);
            c.addToInventory(potion);
            this.collected = true;
        }
        else if (random <= 1.0/2.0 + 1.0/3.0) {
            c.updateTreasurePoint(value);
            this.collected = true;
        }
        else {
            description = "Power potion";
            PowerPotion powerPotion = new PowerPotion(this.getPosition(),false,description,5,1);
            c.addToInventory(powerPotion);
            this.collected = true;
        }
    }

    @Override
    public String toString() {
        if (collected) {
            return "treasure collected!";
        }
        else {
            return "treasure not collected yet!";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Treasure) {
            Treasure other = (Treasure) obj;
            return other.value == this.value && this.getPosition().equals(other.getPosition());
        }
        return false;
    }
}
