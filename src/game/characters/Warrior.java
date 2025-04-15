package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;
import java.util.Random;

public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker {
    private final int defence;
    public Warrior(String playerName, Position position,int health) {
        super(playerName, position, health);
        Random random = new Random();
        this.defence = random.nextInt(120);
    }

    @Override
    public String getDisplaySymbol() {
        return "W";
    }

    @Override
    public MagicElement getMagicElement() {
        return null;
    }


    @Override
    public void fightClose (Combatant target) {
        if (isInMeleeRange(this.getPosition(), target.getPosition())) {
            attack(target);
        } else {
            System.out.println("Target is out of melee range.");
        }
    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        if (self.distanceTo(target) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void attack(Combatant target) {
        if (target.isDead()) {
            System.out.println("Target is already dead.");
            return;
        }
        int damage = this.getPower();
        target.receiveDamage(damage, this);
        System.out.println("You attacked the target for " + damage + " damage.");
    }

    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        if (rand <= 0.1){
            return true;
        }
        return false;
    }

    public void receiveDamage (int amount, Combatant source) {
        if (tryEvade()) {
            System.out.println("You have evaded the attack!");
        }
        else {
            if (isCriticalHit()) {
                amount = amount*2;
            }
            int damage =(int)(amount* (1 - Math.min(0.6, defence/200.0)));
            this.setHealth(this.getHealth() - damage);
        }
    }

    public boolean equals (Object obj) {
        if (obj instanceof Warrior){
            Warrior other = (Warrior) obj;
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }
    public String toString () {
        return "Warrior: " + this.getName() + " at " + this.getPosition();
    }
}
