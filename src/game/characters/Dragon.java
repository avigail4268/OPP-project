package game.characters;

import game.combat.*;
import game.map.Position;

public class Dragon extends Enemy implements PhysicalAttacker, MeleeFighter, MagicAttacker {

    private MagicElement element;

    public Dragon(Position pos, int health) {
        super(pos,health);
        this.element = getElement();
    }

    @Override
    public String getDisplaySymbol() {
        return "";
    }

    @Override
    public MagicElement getMagicElement() {
        return null;
    }

    @Override
    public void calculateMagicDamage(Combatant target) {

    }

    @Override
    public void castSpell(Combatant target) {

    }

    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return false;
    }

    @Override
    public MagicElement getElement() {
        return null;
    }

    @Override
    public void fightClose(Combatant target) {

    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return false;
    }

    @Override
    public void attack(Combatant target) {

    }

    @Override
    public boolean isCriticalHit() {
        return false;
    }
}
