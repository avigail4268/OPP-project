package game.items;


import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

public class Treasure extends GameItem implements Interactable {


    public Treasure(Position position, boolean blocksMovement, String description, int value) {
        super(position,blocksMovement,description);
        this.value = value;
        collected = false;
    }

    @Override
    public String getDisplaySymbol() {
        return "TREASURE";
    }

    public void interact(PlayerCharacter c) {
        double random = Math.random();
        String description;
        if (random <= 1.0/3.0) {
            description = "Health potion";
            Potion potion = new Potion(this.getPosition(),false,description,50,10);
            potion.collect(c);
            this.collected = true;
        }
        else if (random <= 1.0/2.0 + 1.0/3.0) {
            c.updateTreasurePoint(value);
            System.out.println("You collected " + value + " treasure point!");
            this.collected = true;
        }
        else {
            description = "Power potion";
            PowerPotion powerPotion = new PowerPotion(this.getPosition(),false,description,5,1);
            powerPotion.collect(c);
            this.collected = true;
        }
    }
    public void collect(PlayerCharacter c) {
        this.interact(c);
    }

    @Override
    public String toString() {
        return "treasure in position " + getPosition();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Treasure other) {
            return other.value == this.value && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    private int value;
    private boolean collected;
}
