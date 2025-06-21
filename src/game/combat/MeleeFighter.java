package game.combat;

import game.map.Position;

/**
 * The MeleeFighter interface defines the contract for any character
 * that is capable of engaging in close-range (melee) combat.
 * Implementing classes should define how melee combat is executed and how
 * range is determined based on positions on the map.
 */
public interface MeleeFighter {

    /**
     * Executes a melee attack on the specified target.
     * The method should verify if the target is in melee range before attacking.
     * The actual damage logic is typically delegated to the attack method of the attacker.
     * @param target the combatant to engage in close-range combat
     */
    void fightClose(Combatant target);

    /**
     * Checks if the given target is within melee attack range based on positions.
     * @param self   the position of the melee fighter
     * @param target the position of the target
     * @return true if the target is within melee range; false otherwise
     */
    boolean isInMeleeRange(Position self, Position target);
}
