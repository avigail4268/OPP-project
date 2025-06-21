package game.combat;

import game.map.Position;

/**
 * The RangeFighter interface defines behavior for combatants
 * who can engage in ranged attacks.
 * Implementing classes should specify their attack range and provide logic
 * for determining whether a target is within that range.
 */
public interface RangeFighter {

    /**
     * Executes a ranged attack on the given target.
     * This method should include the logic for checking range
     * and applying damage or effects to the target.
     * @param target the Combatant to be attacked
     */
    void fightRanged(Combatant target);

    /**
     * Returns the maximum range within which this fighter
     * can engage in ranged combat.
     * @return the attack range as an integer
     */
    int getRange();

    /**
     * Determines whether a target is within attack range based on positions.
     * @param self   the current position of the attacker
     * @param target the position of the target
     * @return true if the target is within range; false otherwise
     */
    boolean isInRange(Position self, Position target);
}
