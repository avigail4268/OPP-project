package game.characters;

import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.MagicElement;
import game.combat.RangeFighter;
import game.map.Position;


public class Mage extends PlayerCharacter implements RangeFighter, MagicAttacker {
    private MagicElement element;

    public Mage(String name,Position pos) {
        super(name,pos);
        this.element = this.element.getElement();
    }

    @Override
    public String getDisplaySymbol() {
        return "M";
    }

    @Override
    public void calculateMagicDamage(Combatant target) {
    //todo (problem: what if my target is not mage or dragon?)
    }

    @Override
    public void castSpell(Combatant target) {
        //todo : use calculateMagicDamage?
    }

    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        if (element.isStrongerThan(other.getElement())){
            return true;
        }
        return false;
    }

    @Override
    public MagicElement getElement() {
        return element;
    }

    @Override
    public void fightRanged(Combatant target) {
        // todo recheck
        if (isInRange(this.getPosition(), target.getPosition())){
            castSpell(target);
        }
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        if(self.distanceTo(target) <= getRange()){
            return true;
        }
        return false;
    }
}
