package game.characterBuilders;
import game.characters.*;
import game.map.Position;
import java.util.Random;

public class EnemyBuilder implements CharacterBuilder {
    private int health;
    private int power;
    private final Random r = new Random();
    private  Enemy enemy;

    public EnemyBuilder() {
        this.health = 50;
        this.power = r.nextInt(4,14);
    }
    @Override
    public void buildPower(int power) {
        enemy.setPower(power);
    }

    @Override
    public void buildHealth(int health) {
        enemy.setHealth(health);
    }

    public AbstractCharacter getCharacter(){
        return enemy;
    }

    public void randomizeStats() {
        int total = health + power;
        if (r.nextBoolean()) {
            buildHealth(r.nextInt( health - 2, health + 3));
            buildPower(Math.max(1, total - enemy.getHealth()));
        } else {
            buildPower(r.nextInt(Math.max(1, power - 2), power + 3));
            buildHealth( total - enemy.getPower());
        }
    }

    public void build (String type,Position position) {
        switch (type) {
            case "Goblin" -> enemy = new Goblin(position, health);
            case "Orc" -> enemy = new Orc(position, health);
            case "Dragon" -> enemy = new Dragon(position, health);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

}
