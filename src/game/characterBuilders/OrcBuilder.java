package game.characterBuilders;
import game.characters.*;
import game.map.Position;
import java.util.Random;

public class OrcBuilder implements CharacterBuilder {
    private Orc orc;
    private final Random r = new Random();

    public OrcBuilder() {
        this.orc = new Orc(new Position(0, 0), 50); // Default position and health
    }
    @Override
    public void buildPosition(Position pos) {
        orc.setPosition(pos);
    }

    @Override
    public void buildPower(int power) {
        orc.setPower(power);
    }

    @Override
    public void buildHealth(int health) {
        orc.setHealth(health);
    }


    @Override
    public void randomizeStats() {
        int health = orc.getHealth();
        int power = orc.getPower();
        int total = health + power;
        if (r.nextBoolean()) {
            buildHealth(r.nextInt(Math.max(1, health - 2), health + 3));
            buildPower(Math.max(1, total - orc.getHealth()));
        } else {
            buildPower(r.nextInt(Math.max(1, power - 2), power + 3));
            buildHealth(Math.max(1, total - orc.getPower()));
        }
    }

    public AbstractCharacter build () {
        return orc;
    }
}
