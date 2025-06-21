package game.characterBuilders;
import game.characters.*;
import game.map.Position;
import java.util.Random;

/**
 * Builder class for creating enemy characters in the game.
 * Implements the CharacterBuilder interface to set up enemy attributes.
 */
public class EnemyBuilder implements CharacterBuilder {

    /**
     * Default constructor for EnemyBuilder.
     * Initializes the enemy with default health and power values.
     */
    public EnemyBuilder() {
        this.health = 50;
        this.power = r.nextInt(4,14);
    }

    /**
     * Builds the enemy power
     * Initializes the power of the enemy character.
     * @param power The power level to set for the enemy character.
     */
    @Override
    public void buildPower(int power) {
        enemy.setPower(power);
    }

    /**
     * Builds the enemy health
     * Initializes the health of the enemy character.
     * @param health The health points to set for the enemy character.
     */
    @Override
    public void buildHealth(int health) {
        enemy.setHealth(health);
    }

    /**
     * Retrieves the enemy character that has been built.
     * @return The constructed enemy character.
     */
    public AbstractCharacter getCharacter(){
        return enemy;
    }

    /**
     * Randomizes the stats of the enemy character.
     * Adjusts health and power based on a random choice, ensuring they sum to a total.
     */
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

    /**
     * Builds an enemy character of a specified type at a given position.
     * Initializes the enemy with the specified health.
     * @param type The type of enemy to create (e.g., "Goblin", "Orc", "Dragon").
     * @param position The position where the enemy will be placed on the map.
     */
    public void build (String type,Position position) {
        switch (type) {
            case "Goblin" -> enemy = new Goblin(position, health);
            case "Orc" -> enemy = new Orc(position, health);
            case "Dragon" -> enemy = new Dragon(position, health);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    // --- Fields ---
    private int health;
    private int power;
    private final Random r = new Random();
    private  Enemy enemy;

}
