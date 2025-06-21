package game.combat;

/**
 * The PhysicalAttacker interface defines the contract for characters or entities
 * that can perform physical (non-magical) attacks in combat.
 * This interface typically applies to melee or ranged attackers that use raw power
 * rather than magical abilities to inflict damage.
 */
public interface PhysicalAttacker {

    /**
     * Performs a physical attack on the specified target.
     * The damage dealt may vary depending on whether the attack is a critical hit.
     * This method should contain the logic for calculating and applying damage.
     * @param target the Combatant receiving the attack
     */
    void attack(Combatant target);

    /**
     * Determines whether the next physical attack will be a critical hit.
     * Critical hits typically deal more damage than standard attacks.
     * @return true if the attack is a critical hit; false otherwise
     */
    boolean isCriticalHit();

}
