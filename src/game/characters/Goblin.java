package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Map;
import java.util.Random;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter {

    private int agility;

    public Goblin(Position pos, int health) {
        super(pos,health);
        agility = new Random().nextInt(80);
    }
    public int getAgility() {
        return agility;
    }
    @Override
    public String getDisplaySymbol() {
        return "G";
    }

    @Override
    public MagicElement getMagicElement() {
        //TODO: goblins don't have magic element
        return null;
    }

    @Override
    //TODO: check if this is correct
    public void fightClose(Combatant target) {
        if (this.isInMeleeRange(this.getPosition(), target.getPosition()))
        {
            this.attack(target);
        }
    }

    @Override
    //TODO: check if this is correct
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }

    public boolean tryEvade() {
        //TODO : check if this is correct
        double evadeChance = Math.min(0.8, agility / 100.0);
        return evadeChance <= 0.25;
    }

    @Override
    //TODO : check if this is correct and possible todo
    public void attack(Combatant target) {
        if (target.tryEvade()) {
            System.out.println("The target evaded the attack!");
        }
        else if (isCriticalHit()) {
            System.out.println("Critical hit!");
            target.receiveDamage(getPower() * 2, this);
        } else {
            target.receiveDamage(getPower(), this);
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
       if (obj instanceof Goblin) {
            Goblin goblin = (Goblin) obj;
            return this.getPosition() == goblin.getPosition() && this.getAgility() == goblin.getAgility();
       }
        return false;
    }
    @Override
    public String toString () {
        return "Goblin at this position " + this.getPosition();
    }
}