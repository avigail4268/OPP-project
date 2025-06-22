package game.decorator;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.core.GameEntity;
import game.map.Position;

import java.util.List;

/**
 * EnemyDecorator is an abstract class that serves as a base for decorators that enhance or modify the behavior of an Enemy.
 * It extends the Enemy class and implements the GameEntity interface.
 * The decorator pattern allows for dynamic addition of responsibilities to objects.
 */
public abstract class EnemyDecorator extends Enemy {

    /**
     * Constructs a new EnemyDecorator with the specified Enemy.
     * @param enemy the Enemy to be decorated
     */
    public EnemyDecorator(Enemy enemy) {
        super(enemy);
        this.decoratorEnemy = enemy;
    }

    /**
     * Gets the decorated Enemy instance.
     * @return the Enemy being decorated
     */
    public Enemy getDecoratorEnemy() {
        return decoratorEnemy;
    }

    /**
     * Make a deep copy of the decorated Enemy.
     */
    @Override
    public GameEntity deepCopy() {
        return decoratorEnemy.deepCopy();
    }

    /**
     * Attacks the target Combatant using the decorated Enemy's attack method.
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        decoratorEnemy.attack(target);
    }

    /**
     * Receives damage from a source Combatant.
     * @param amount the amount of damage to receive
     * @param source the Combatant that is dealing the damage
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        decoratorEnemy.receiveDamage(amount, source);
    }

    /**
     * Returns the display symbol representing the Enemy.
     */
    @Override
    public String getDisplaySymbol() {
        return decoratorEnemy.getDisplaySymbol();
    }

    /**
     * Checks if the target is within range of the Enemy.
     * @param self the position of the Enemy
     * @param target the position of the target
     * @return true if the target is in range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return decoratorEnemy.isInRange(self, target);
    }

    /**
     *returns the current health of the Enemy.
     */
    @Override
    public int getHealth() {
        return decoratorEnemy.getHealth();
    }

    /**
     * Sets the health of the Enemy.
     * @param health the new health value to set
     * @return true if the health was successfully updated, false otherwise
     */
    @Override
    public boolean setHealth(int health) {
        return decoratorEnemy.setHealth(health);
    }

    /**
     * Checks if the Enemy is dead.
     * @return true if the Enemy's health is zero or less, false otherwise
     */
    @Override
    public boolean isDead() {
        return decoratorEnemy.isDead();
    }

    // --- Fields ---
    private final Enemy decoratorEnemy;

}

