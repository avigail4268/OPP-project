package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.PhysicalAttacker;
import game.combat.RangeFighter;
import game.map.Position;

import java.util.Random;

public class Archer extends PlayerCharacter implements RangeFighter, PhysicalAttacker {

    public Archer(String name, Position pos, int health) {
        super(name,pos,health);
        Random rand = new Random();
        accuracy = rand.nextDouble(0.8);
    }
    @Override
    public String getDisplaySymbol() {
        return "Archer";
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
    }
    @Override
    public void attack(Combatant target) {
        int damage = this.getPower();
        if (isCriticalHit()) {
            target.receiveDamage(getPower() * 2, this);
            System.out.println("Archer attacked with Critical hit! for " + getPower() * 2 + " damage.");
        }
        else
        {
            target.receiveDamage(getPower(), this);
            System.out.println(this.getName() + " attacked the enemy for " + damage + " damage.");
        }
    }

    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        if (rand <= 0.1){
            return true;
        }
        return false;
    }
    @Override
    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())){
            attack(target);
        }
    }
    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }
    @Override
    public int getRange() {
        //TODO : this method!!!
        return 2;
    }
    public boolean equals (Object obj) {
        if (obj instanceof Archer) {
            Archer other = (Archer) obj;
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }


    public String toString () {
        return "Archer: " + this.getName() + " at " + this.getPosition();
    }

    private double accuracy;

}
