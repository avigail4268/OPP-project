package game.characters;

import game.combat.*;
import game.map.Position;

public class Dragon extends Enemy implements PhysicalAttacker, MeleeFighter, MagicAttacker {

    private MagicElement element;

    public Dragon(Position pos, int health) {
        super(pos,health);
        element = getMagicElement();
        //TODO: check if this is correct, i think the element should be set in the constructor
        // cause its not innitialized

    }

    @Override
    public String getDisplaySymbol() {
        return "D";
    }

    @Override
    public MagicElement getMagicElement() {
        return element;
    }

    @Override
    //TODO: check if this is correct
    public void calculateMagicDamage(Combatant target) {
        if (target.getMagicElement() == null) {
            return;
        }
        if (this.element.isElementStrongerThan(target.getMagicElement())) {
            double totalDamage = this.getPower() * 1.5;
            target.receiveDamage((int) totalDamage, this);
        } else if (target.getMagicElement().isElementStrongerThan(this.element)) {
            double totalDamage = this.getPower() * 0.8;
            target.receiveDamage((int) totalDamage, this);
        } else {
            double totalDamage = this.getPower();
            target.receiveDamage((int) totalDamage, this);
        }
    }

    @Override
    public void castSpell (Combatant target) {
        //TODO: check if this is correct
        double powerAttack = this.getPower() * 1.5;
        if (target.getMagicElement() == null) {
            target.receiveDamage((int) powerAttack, this);
        } else {
            if (this.element.isElementStrongerThan(target.getMagicElement())) {
                powerAttack *= 1.2;
            } else if (target.getMagicElement().isElementStrongerThan(this.element)) {
                powerAttack *= 0.8;
            }
            target.receiveDamage((int) powerAttack, this);
        }
    }

    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        //TODO: check if this is correct
        if (element.isElementStrongerThan(other.getMagicElement())){
            return true;
        }
        return false;
    }

    @Override
    public void fightClose(Combatant target) {
        //TODO: check if this is correct
        if (isInMeleeRange(this.getPosition(), target.getPosition())) {
            double powerAttack = this.getPower();
            if (isCriticalHit())
            {
                target.receiveDamage((int) powerAttack * 2, this);

            }
            else {
                target.receiveDamage((int) powerAttack, this);
            }
        }
    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }

    @Override
    public void attack(Combatant target) {
        //TODO: check if this is correct
        if (isInMeleeRange(this.getPosition(), target.getPosition())) {
            castSpell(target);
        }
        else {
            fightClose(target);
        }
    }

    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Dragon) {
            Dragon dragon = (Dragon) obj;
            return this.getPosition() == dragon.getPosition() && this.getMagicElement() == dragon.getMagicElement();
        }
        return false;
    }
    @Override
    public String toString () {
        return "Dragon at position " + getPosition() + " and magic element " + getMagicElement();
    }
}
