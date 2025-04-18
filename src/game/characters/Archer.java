package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.PhysicalAttacker;
import game.combat.RangeFighter;
import game.map.Position;

import java.util.Random;

public class Archer extends PlayerCharacter implements RangeFighter, PhysicalAttacker {
    private double accuracy;

    public Archer(String name, Position pos, int health) {
        super(name,pos,health);
        Random rand = new Random();
        accuracy = rand.nextDouble(0.8);
    }

    @Override
    public String getDisplaySymbol() {
        return "A";
    }

    @Override
    public MagicElement getMagicElement() { //non magic element
        return null;
    }

    public boolean tryEvade(){
        //TODO : check if this is correct
        double enemyEvasion = getEvasionChance();
        double evadeChance = enemyEvasion * (1 - accuracy);
        return evadeChance <= 0.25;
//        double evadeChance = Math.random();
//        if (evadeChance < this.)
//        return false;
    }
    @Override
    public void attack(Combatant target) {
        //todo : how to check if manage to evade?
        if (target.isDead())
        {
            System.out.println("Target is already dead.");
        }
        int damage = this.getPower();
        target.receiveDamage(damage, this);
        System.out.println("You attacked the target for " + damage + " damage.");
    }

    @Override
    public boolean isCriticalHit() {
        // todo : double code??????
        double rand = Math.random();
        if (rand <= 0.1){
            return true;
        }
        return false;
    }

    @Override
    public void fightRanged(Combatant target) {
        //TODO : check if this is correct
        if (isInRange(this.getPosition(), target.getPosition())){
            attack(target);
        }
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        //TODO the same code as Mage???!!!!
        //TODO : check if this is correct
        return self.distanceTo(target) <= getRange();
    }

    @Override
    public int getRange() {
        //TODO : this method!!!
        return 2;
    }

    public boolean equals (Object obj) {
        if (obj instanceof Archer) {
            Archer archer = (Archer) obj;
            return this.getName().equals(archer.getName()) && this.getPosition().equals(archer.getPosition());
        }
        return false;
    }
    public String toString () {
        return "Archer: " + this.getName() + " at " + this.getPosition();
    }
}
