package game.characters;

import game.combat.Combatant;
import game.combat.PhysicalAttacker;
import game.combat.RangeFighter;
import game.map.Position;

import java.util.Random;

public class Archer extends PlayerCharacter implements RangeFighter, PhysicalAttacker {
    private double accuracy;

    public Archer(String name, Position pos) {
        super(name, pos);
        Random rand = new Random();
        accuracy = rand.nextDouble(0.8);
    }

    @Override
    public String getDisplaySymbol() {
        return "A";
    }

    @Override
    public void attack(Combatant target) {
        //todo : how to check if manage to evade?
        if (target.tryEvade()){
            System.out.println("You are evading.");
        }
        else {
            target.receiveDamage(getPower(),this);
        }
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

    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        //TODO the same code as Mage???!!!!
        return false;
    }
}
