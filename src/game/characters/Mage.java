package game.characters;

import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.MagicElement;
import game.combat.RangeFighter;
import game.map.Position;


public class Mage extends PlayerCharacter implements RangeFighter, MagicAttacker {
    private MagicElement element;

    public Mage(String name,Position pos,int health) {
        super(name,pos,health);
        this.element = this.element.getElement();
    }

    @Override
    public String getDisplaySymbol() {
        return "M";
    }

    @Override
    public MagicElement getMagicElement() {
        return element;
    }

    @Override
    public void calculateMagicDamage(Combatant target) {
        double totalDamage = this.getPower() * 1.5;
        MagicElement targetElement = target.getMagicElement();
        if (targetElement != null) {
            if (this.element.isStrongerThan(targetElement)) {
                totalDamage *= 1.2;
            } else if (targetElement.isStrongerThan(this.element)) {
                totalDamage *= 0.8;
            }
        }
        target.receiveDamage((int) totalDamage, this);
    }

    @Override
    public void castSpell(Combatant target) {
        //todo : use calculateMagicDamage?
        calculateMagicDamage(target);
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
