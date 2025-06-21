package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.core.GameEntity;
import game.map.Position;
import java.util.Random;

/**
 * AbstractCharacter is a base class for all characters in the game world.
 * It implements basic combat and game entity functionalities such as health, position,
 * power, and visibility.
 */
public abstract class AbstractCharacter implements Combatant, GameEntity {

    /**
     * Constructs an AbstractCharacter with a given position and initial health.
     * Power is randomly generated between 4 (inclusive) and 14 (exclusive).
     * @param position the initial position of the character
     * @param health the initial health of the character
     */
    public AbstractCharacter(Position position, int health) {
        setPosition(position);
        this.health = health;
        this.power = new Random().nextInt(4,14);
        this.visible = true;
    }

    /**
     * Gets the maximum health of the character.
     * @return the maximum health value (implemented by subclasses)
     */
    public abstract int getMaxHealth();

    /**
     * Increases the power of the character by the given amount if positive.
     * @param amount the amount to add to power
     */
    public void setPower(int amount) {
        this.power += amount;
    }

    /**
     * Heals the character by a specified amount.
     * @param amount the amount to heal
     */
    @Override
    public void heal(int amount) {
        this.health += amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Checks if the character is dead (health is 0 or less).
     * @return true if dead, false otherwise
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Gets the power value of the character.
     * @return the character's power
     */
    @Override
    public int getPower() {
        return this.power;
    }

    /**
     * Determines whether the character successfully evades an attack by a random number between 0 and 1.
     * @return true if evasion is successful, false otherwise
     */
    @Override
    public boolean tryEvade() {
        double evadeChance = Math.random();
        return evadeChance < this.evasionChance;
    }

    /**
     * Gets the current position of the character.
     * @return the character's position
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Sets a new position for the character if the position is not null.
     * @param newPos the new position to set
     * @return true if the position was updated, false otherwise
     */
    @Override
    public boolean setPosition(Position newPos) {
        if (newPos != null) {
            this.position = newPos;
            return true;
        }
        return false;
    }

    /**
     * Sets the character's health if the given value is positive.
     * @param health the new health value
     * @return true if the health was updated, false otherwise
     */
    @Override
    public boolean setHealth(int health) {
        this.health = Math.min(health, getMaxHealth()); // כדי שלא תחרג
        return true;
    }

    /**
     * Checks if the character is currently visible on the map.
     * @return true if visible, false otherwise
     */
    @Override
    public boolean getVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the character.
     * @param visible the new visibility state
     */
    @Override
    public void setVisible(boolean visible){
        this.visible = visible;
    }

    /**
     * Gets the current health of the character.
     * @return the health value
     */
    @Override
    public int getHealth(){
        if (health >= 0) {
            return health;
        }
        return 0;
    }

    /**
     * Gets the magic element associated with the character.
     * @return the character's magic element (implemented by subclasses)
     */
    @Override
    public MagicElement getMagicElement()
    {
        return null; // Default implementation, subclasses should override
    }

    /**
     * Applies incoming damage to the character.
     * @param amount the amount of damage
     * @param source the attacker
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        setHealth(Math.max(0, getHealth() - amount));
    }

    /**
     * Gets the display symbol representing the character on the map.
     * @return the display symbol (implemented by subclasses)
     */
    @Override
    public abstract String getDisplaySymbol();

    /**
     * Performs an attack on the given target.
     * @param target the combatant being attacked
     */
    @Override
    public abstract void attack(Combatant target);

    /**
     * Checks if the target is within the attack range of the character.
     * @param self the character itself
     * @param target the target position
     * @return true if in range, false otherwise
     */
    @Override
    public abstract boolean isInRange (Position self, Position target);

    // --- Fields ---

    private Position position;
    private int health;
    private int power;
    private boolean visible;
    private final double evasionChance = 0.25;
}