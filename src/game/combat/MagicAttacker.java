package game.combat;

public interface MagicAttacker {
    void calculateMagicDamage(Combatant target);
    void castSpell(Combatant target);
    boolean isElementStrongerThan(MagicAttacker other);
    MagicElement getMagicElement();
}
