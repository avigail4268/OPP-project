package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;
import java.util.Random;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter {
    public Goblin(Position pos, int health) {
        super(pos,health);
        agility = new Random().nextInt(80);
    }
    public int getAgility() {
        return agility;
    }
    @Override
    public String getDisplaySymbol() {
        return "Goblin";
    }
    @Override
    public MagicElement getMagicElement() {
        //TODO: goblins don't have magic element
        return null;
    }
    @Override
    public void fightClose(Combatant target) {
        if (this.isInMeleeRange(this.getPosition(), target.getPosition()))
        {
            this.attack(target);
        }
    }
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }
    public boolean tryEvade() {
        //TODO : check if this is correct
        double evadeChance = Math.min(0.8, agility / 100.0);
        return evadeChance <= 0.25;
    }
    @Override
    public void attack(Combatant target) {
        if (isCriticalHit()) {
            target.receiveDamage(getPower() * 2, this);
            System.out.println("The goblin attack back with Critical hit!");
        }
        else {
            target.receiveDamage(getPower(), this);
        }
    }
//original function
//    @Override
//    public void attack(Combatant target) {
//        if (isCriticalHit()) {
//            if(target.receiveDamage(getPower() * 2, this))
//            {
//                System.out.println("The goblin attack back with Critical hit!");
//            }
//        }
//        else {
//            target.receiveDamage(getPower(), this);
//        }
//    }
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

    private int agility;

}