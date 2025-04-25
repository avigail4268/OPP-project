package game.combat;

/**
 * The interface defines behaviors specific to characters
 * capable of performing magical attacks in the game.
 * Implementing classes are expected to cast spells, calculate elemental damage,
 * and compare their magic element's strength relative to others.
 */
public interface MagicAttacker {

    /**
     * Calculates and applies magic damage to the specified target.
     * This method is responsible for determining the effective damage
     * based on the attacker's power and the elemental relationship between
     * the attacker and the target. It typically applies the damage immediately.
     *
     * @param target the combatant receiving the magical attack
     */
    void calculateMagicDamage(Combatant target);

    /**
     * Casts a spell on the specified target.
     * This method often delegates the damage calculation to
     * calculateMagicDamage(Combatant), but can include additional
     * spellcasting behavior such as animations, status effects, or resource usage.
     *
     * @param target the combatant targeted by the spell
     */
    void castSpell(Combatant target);

    /**
     * Compares the magic element of this attacker with another to determine
     * if this attacker's element is stronger.
     *
     * @param other the other magic attacker to compare against
     * @return true if this attacker's element is stronger than the other's;
     * false otherwise
     */
    boolean isElementStrongerThan(MagicAttacker other);

    /**
     * Returns the magic element associated with this attacker.
     * This may be used to determine elemental affinities and weaknesses during combat.
     *
     * @return the MagicElement of this attacker
     */
    MagicElement getMagicElement();
}
