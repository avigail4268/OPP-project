package game.items;
import game.characters.PlayerCharacter;
import game.map.Position;

public class Treasure extends GameItem implements Interactable {
    public Treasure(Position position, boolean blocksMovement, int value) {
        super(position,blocksMovement);
        this.value = value;
        collected = false;
        this.setDescription("This is a Treasure! it might be a health potion/power potion/treasure points");
    }

    @Override
    public String getDisplaySymbol() {
        return "TREASURE";
    }
    @Override
    public void interact(PlayerCharacter c) {
        double random = Math.random();
        if (random <= 1.0/3.0) {
            Potion potion = new Potion(this.getPosition(),false,50,10);
            potion.collect(c);
            this.collected = true;
        }
        else if (random <= 1.0/2.0 + 1.0/3.0) {
            c.updateTreasurePoint(value);
            System.out.println("You collected " + value + " treasure point!");
            this.collected = true;
        }
        else {
            PowerPotion powerPotion = new PowerPotion(this.getPosition(),false,5,1);
            powerPotion.collect(c);
            this.collected = true;
        }
    }
    @Override
    public void collect(PlayerCharacter c) {
        if (!collected) {
            this.interact(c);
        }
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
    @Override
    public String getColorCode() {
        return "\u001B[34m"; // Blue for treasure
    }
    private final int value;
    private boolean collected;
}
