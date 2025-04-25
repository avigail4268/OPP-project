package game.combat;
import game.map.Position;

/**
 * The Combatant interface defines the behavior and properties of any entity that can
 * participate in combat within the game. It includes health management, attack mechanics,
 * evasion, and position tracking.
 */
public interface Combatant {

    /**
     * Retrieves the current health of the combatant.
     *
     * @return the current health value
     */
    int getHealth();

    /**
     * Sets the health of the combatant. This method may be used to update the combatant's health.
     *
     * @param health the new health value
     * @return true if the health was successfully set, false otherwise
     */
    boolean setHealth(int health);

    /**
     * Applies damage to this combatant, factoring in any relevant effects or resistances.
     *
     * @param amount the amount of damage to apply
     * @param source the source of the damage (attacker)
     */
    void receiveDamage(int amount, Combatant source);

    /**
     * Restores a certain amount of health to the combatant.
     *
     * @param amount the amount of health to restore
     */
    void heal(int amount);

    /**
     * Checks whether the combatant is dead (i.e., health is zero or below).
     *
     * @return true if dead, false otherwise
     */
    boolean isDead();

    /**
     * Retrieves the base power of the combatant, which may be used in attack calculations.
     *
     * @return the power stat of the combatant
     */
    int getPower();

    /**
     * Attempts to evade an incoming attack. The implementation may use randomness or attributes.
     *
     * @return true if the attack was successfully evaded, false otherwise
     */
    boolean tryEvade();

    /**
     * Retrieves the current position of the combatant on the game map.
     *
     * @return the Position object representing the combatant's location
     */
    Position getPosition();

    /**
     * Retrieves the magical element of the combatant, if any. Returns null if the combatant is
     * not associated with a magic element.
     *
     * @return the combatant's MagicElement, or null if none
     */
    MagicElement getMagicElement();

    /**
     * Performs an attack on the given target. The behavior may vary depending on the implementation
     *
     * @param target the combatant being attacked
     */
    void attack(Combatant target);
    /**
     * Checks if the combatant is in melee range of the target position.

     * @return true if in melee range, false otherwise
     */
    boolean isInRange(Position self, Position target);

}
