package game.characterBuilders;
import game.characters.*;
import game.map.Position;
import java.util.Random;

public class DragonBuilder implements CharacterBuilder {
    private Dragon dragon;
    private final Random r = new Random();

    public DragonBuilder() {
        this.dragon =  new Dragon(new Position(0, 0), 50); // Default position and health
    }
    @Override
    public void buildPosition(Position pos) {
        dragon.setPosition(pos);
    }

    @Override
    public void buildPower(int power) {
        dragon.setPower(power);
    }

    @Override
    public void buildHealth(int health) {
        dragon.setHealth(health);
    }


    @Override
    public void randomizeStats() {
        int health = dragon.getHealth();
        int power = dragon.getPower();
        int total = health + power;
        if (r.nextBoolean()) {
            buildHealth(r.nextInt(Math.max(1, health - 2), health + 3));
            buildPower(Math.max(1, total - dragon.getHealth()));
        } else {
            buildPower(r.nextInt(Math.max(1, power - 2), power + 3));
            buildHealth(Math.max(1, total - dragon.getPower()));
        }
    }

    public AbstractCharacter build () {
        return dragon;
    }
}
