package game.characters;

import game.combat.MagicElement;
import game.combat.*;
import game.map.Position;

public class Dragon extends Enemy implements PhysicalAttacker, MeleeFighter, MagicAttacker {
    public Dragon(Position pos, int health) {
        super(pos,health);
         this.element = MagicElement.getElement();
    }
    @Override
    public String getDisplaySymbol() {
        return "DRAGON";
    }
    @Override
    public MagicElement getMagicElement() {
        return element;
    }
    @Override
    public void calculateMagicDamage(Combatant target) {
        if (target.getMagicElement() == null) {
            target.receiveDamage(this.getPower(), this);
            System.out.println("dragon calcmagic, attack physical ");

        }
        else {
            if (this.element.isElementStrongerThan(target.getMagicElement())) {
                double totalDamage = this.getPower() * 1.5;
                target.receiveDamage((int) totalDamage, this);
                System.out.println("dragon calcmagic,his element stronger than you, attack magic");

            } else if (target.getMagicElement().isElementStrongerThan(this.element)) {
                double totalDamage = this.getPower() * 0.8;
                target.receiveDamage((int) totalDamage, this);
                System.out.println("dragon calcmagic,his element not stronger than you attack magic");

            } else {
                System.out.println("dragon calcmagic,attack magic");
                double totalDamage = this.getPower();
                target.receiveDamage((int) totalDamage, this);
            }
        }
    }
    @Override
    public void castSpell (Combatant target) {
        double powerAttack = this.getPower() * 1.5;
        if (target.getMagicElement() == null) {
            target.receiveDamage((int) powerAttack, this);
            System.out.println("dragon attacked physically for: " + (int) powerAttack);
        }
        else {
            if (this.element.isElementStrongerThan(target.getMagicElement())) {
                powerAttack *= 1.2;
            } else if (target.getMagicElement().isElementStrongerThan(this.element)) {
                powerAttack *= 0.8;
            }
            target.receiveDamage((int) powerAttack, this);
            System.out.println("dragon attacked by magic for: " + (int) powerAttack);
        }
    }
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        if (element.isElementStrongerThan(other.getMagicElement())){
            return true;
        }
        return false;
    }
    @Override
    public void fightClose(Combatant target) {
        if (isInMeleeRange(this.getPosition(), target.getPosition())) {
            double powerAttack = this.getPower();
            if (isCriticalHit())
            {
                target.receiveDamage((int) powerAttack * 2, this);
                System.out.println("The dragon attack back with Critical hit!");
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
        return "Dragon at position " + getPosition();
    }

    private MagicElement element;

}
