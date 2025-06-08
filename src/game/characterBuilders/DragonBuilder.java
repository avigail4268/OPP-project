package game.characterBuilders;
import game.characters.Dragon;
import game.characters.Enemy;
import game.map.Position;
import java.util.Random;

public class DragonBuilder implements CharacterBuilder {
    private int health;
    private int power;
    private Position pos;
    private final Random r = new Random();

    public DragonBuilder() {
        this.health = 50;
        this.power = r.nextInt(4, 14);
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void randomizeStats() {
        int total = health + power;
        if (r.nextBoolean()) {
            health = r.nextInt(Math.max(1, health - 2), health + 3);
            power = Math.max(1, total - health);
        } else {
            power = r.nextInt(Math.max(1, power - 2), power + 3);
            health = Math.max(1, total - power);
        }
    }

    @Override
    public Enemy build() {
        return new Dragon(pos, health);
    }
}
