package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

public class Orc extends Enemy implements MeleeFighter, PhysicalAttacker {

    private double resistance;

    public Orc(Position pos, int health) {
        super(pos,health);
        resistance = new Random().nextDouble(0.5);
    }
    public void receiveDamage(int amount, Combatant source) {
        //TODO: check if this is correct
        //if orc is not dead calc the damage if he is dead then give loot
        if (!this.isDead()) {
            System.out.println("Orc received damage: " + amount);
            if (source.getMagicElement() != null) {
                double magicDamage = amount * (1 - resistance);
                super.receiveDamage((int) magicDamage, source);
            } else {
                super.receiveDamage(amount, source);
            }
        }
        else {
            System.out.println("Orc is dead.");
            this.defeat();
            if(source instanceof Mage)
            {
                Mage mage = (Mage) source;
                mage.updateTreasurePoint(this.getLoot());
            }
            else if (source instanceof Archer)
            {
                Archer archer = (Archer) source;
                archer.updateTreasurePoint(this.getLoot());
            }
            else if (source instanceof Warrior)
            {
                Warrior warrior = (Warrior) source;
                warrior.updateTreasurePoint(this.getLoot());
            }
        }
    }
    public double getResistance() {
        return resistance;
    }

    @Override
    public String getDisplaySymbol() {
        return "O";
    }

    @Override
    public MagicElement getMagicElement() { //non magic element
        return null;
    }

    @Override
    public void fightClose(Combatant target) {
        //TODO: check if this is correct
        //TODO: double code?
        if (this.isInMeleeRange(this.getPosition(), target.getPosition())) {
            this.attack(target);
        }
    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        //TODO: check if this is correct
        return self.distanceTo(target) <= 1;
    }

    @Override
    public void attack(Combatant target) {
        //TODO: check if this is correct
        if (target.tryEvade()) {
            System.out.println("The target evaded the attack!");
        } else
        {
            if (isCriticalHit()) {
                target.receiveDamage(getPower() * 2, this);
            }
            else {
                target.receiveDamage(getPower(), this);
            }
        }
    }
    @Override
    public boolean isCriticalHit() {
        //TODO: check if this is correct
        double rand = Math.random();
        return rand <= 0.1;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Orc) {
            Orc orc = (Orc) obj;
            return this.getPosition() == orc.getPosition() && this.getResistance() == orc.getResistance();
        }
        return false;
    }
    @Override
    public String toString () {
        return "Orc at position " + getPosition() + "and resistance " + getResistance();
    }


}
