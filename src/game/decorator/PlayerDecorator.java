package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.combat.MagicElement;
import game.core.GameEntity;
import game.map.Position;

/**
 * PlayerDecorator is an abstract class that serves as a base for decorators that enhance or modify the behavior of a PlayerCharacter.
 * It extends the PlayerCharacter class and implements the GameEntity interface.
 * The decorator pattern allows for dynamic addition of responsibilities to objects.
 */
public abstract class PlayerDecorator extends PlayerCharacter {

    /**
     * Constructs a new PlayerDecorator with the specified PlayerCharacter.
     * This constructor copies all values from the original player character.
     * @param player the PlayerCharacter to be decorated
     */
    public PlayerDecorator(PlayerCharacter player) {
        super(player);
        this.decoratorPlayer = player;
    }

    /**
     * Gets the decorated PlayerCharacter instance.
     * @return the PlayerCharacter being decorated
     */
    public PlayerCharacter getDecoratorPlayer() {
        return decoratorPlayer;
    }

    /**
     * Unwraps the decorator to get the original PlayerCharacter.
     * This method traverses through the decorators until it finds the base PlayerCharacter.
     * @return the original PlayerCharacter instance
     */
    public PlayerCharacter unwrap() {
        PlayerCharacter current = this;
        while (current instanceof PlayerDecorator decorator) {
            current = decorator.getDecoratorPlayer();
        }
        return current;
    }

    /**
     * Attacks a target Combatant using the decorated PlayerCharacter's attack method.
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        decoratorPlayer.attack(target);
    }

    /**
     * Checks if the PlayerCharacter is in range to attack the target.
     * @param self the position of the PlayerCharacter
     * @param target the position of the target Combatant
     * @return true if the target is within range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return decoratorPlayer.isInRange(self, target);
    }

    /**
     * Make a deep copy of the decorated PlayerCharacter.
     * @return a new GameEntity instance that is a deep copy of the decorated PlayerCharacter
     */
    @Override
    public GameEntity deepCopy() {
        return decoratorPlayer.deepCopy();
    }

    /**
     * Sets the health of the PlayerCharacter.
     * This method updates both the decorator and the underlying PlayerCharacter.
     * @param health the new health value to set
     * @return true if the health was successfully updated, false otherwise
     */
    @Override
    public boolean setHealth(int health) {
        super.setHealth(health); // מעדכן את הדקורטור
        return getDecoratorPlayer().setHealth(health); // מעדכן את השחקן האמיתי
    }

    /**
     * Gets the current health of the PlayerCharacter.
     * This method always returns the health of the decorated PlayerCharacter.
     * @return the current health value
     */
    @Override
    public int getHealth() {
        return getDecoratorPlayer().getHealth(); // always return our own health (UI reads from here)
    }

    /**
     * Gets the magic element of the PlayerCharacter.
     * This method retrieves the magic element from the decorated PlayerCharacter.
     * @return the MagicElement of the PlayerCharacter
     */
    @Override
    public MagicElement getMagicElement() {
        return decoratorPlayer.getMagicElement();
    }

    /**
     * Receives damage from a source Combatant.
     * @return the amount of damage received
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        decoratorPlayer.receiveDamage(amount, source);
    }

    /**
     * Updates the state of the PlayerDecorator.
     * This method calls the update method of the decorated PlayerCharacter
     */
    @Override
    public void update() {
        decoratorPlayer.update();
        this.setHealth(decoratorPlayer.getHealth());
    }

    /**
     * Heals the PlayerCharacter by a specified amount.
     * This method calls the heal method of the decorated PlayerCharacter.
     * @param amount the amount of health to restore
     */
    @Override
    public void heal(int amount) {
        getDecoratorPlayer().heal(amount);
    }

    /**
     * Checks if the PlayerCharacter is dead.
     * This method checks the health of the decorated PlayerCharacter.
     * @return true if the PlayerCharacter's health is less than or equal to 0, false otherwise
     */
    @Override
    public boolean isDead () {
        return getDecoratorPlayer().isDead();
    }

    /**
     * Gets the display symbol of the PlayerCharacter.
     * This method retrieves the display symbol from the decorated PlayerCharacter.
     * @return the display symbol as a string
     */
    @Override
    public String getDisplaySymbol() {
        return decoratorPlayer.getDisplaySymbol();
    }

    // --- Fields ---
    protected final PlayerCharacter decoratorPlayer;

}
