package game.characterBuilders;
import game.characters.*;
import game.map.Position;
import java.util.Random;

public class EnemyBuilder implements CharacterBuilder {
    private int health;
    private int power;
    private final Random r = new Random();
    private  Enemy enemy;
    private Position position;

    public EnemyBuilder() {
        this.health = 50;
        this.power = r.nextInt(4,14);
    }
//    @Override
//    public void buildPosition(Position pos) {
//        enemy.setPosition(pos);
//    }

    @Override
    public void buildPower(int power) {
        enemy.setPower(power);
    }

    @Override
    public void buildHealth(int health) {
        enemy.setHealth(health);
    }


    @Override
    public void randomizeStats() {
        int health = enemy.getHealth();
        int power = enemy.getPower();
        int total = health + power;
        if (r.nextBoolean()) {
            buildHealth(r.nextInt(Math.max(1, health - 2), health + 3));
            buildPower(Math.max(1, total - enemy.getHealth()));
        } else {
            buildPower(r.nextInt(Math.max(1, power - 2), power + 3));
            buildHealth(Math.max(1, total - enemy.getPower()));
        }
    }

    public AbstractCharacter build (String type,Position position) {
        return switch (type) {
            case "Goblin" -> enemy = new Goblin(position, health);
            case "Orc" -> enemy = new Orc(position, health);
            case "Dragon" -> enemy = new Dragon(position, health);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

}
