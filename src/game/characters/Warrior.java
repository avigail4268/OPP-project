//package game.characters;
//import game.combat.Combatant;
//import game.combat.MeleeFighter;
//import game.combat.PhysicalAttacker;
//import game.log.LogManager;
//import game.map.Position;
//import java.util.Random;
//
///**
// * The Warrior class represents a player character specialized in melee and physical combat.
// * Warriors have a chance to land critical hits and can reduce incoming damage using their defense stat.
// */
//public class
//Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker {
//
//    /**
//     * Constructs a Warrior with the given name, position, and health.
//     * The Warrior is assigned a random defense value between 0 and 119.
//     *
//     * @param playerName the name of the Warrior
//     * @param position   the initial position on the map
//     * @param health     the initial health value
//     */
//    public Warrior(String playerName, Position position, int health) {
//        super(playerName, position, health);
//        Random random = new Random();
//        this.defence = random.nextInt(120);
//    }
//
//    /**
//     * Returns the symbol that represents this character on the map.
//     *
//     * @return the string "Warrior"
//     */
//    @Override
//    public String getDisplaySymbol() {
//        return "Warrior";
//    }
//
//    /**
//     * Engages in a close-range fight with a target if within melee range.
//     *
//     * @param target the enemy combatant to fight
//     */
//    @Override
//    public void fightClose(Combatant target) {
//        if (isInRange(this.getPosition(), target.getPosition())) {
//            attack(target);
//        } else {
//            System.out.println("Target is out of melee range.");
//        }
//    }
//
//    /**
//     * Determines whether the target is within melee range (distance == 1).
//     *
//     * @param self   the position of the Warrior
//     * @param target the position of the target
//     * @return true if within range, false otherwise
//     */
//    @Override
//    public boolean isInMeleeRange(Position self, Position target) {
//        return self.distanceTo(target) == 1;
//    }
//    /**
//     * Checks if the target is within range for a melee attack.
//     *
//     * @param self   the position of the Warrior
//     * @param target the position of the target
//     * @return true if within range, false otherwise
//     */
//    @Override
//    public boolean isInRange(Position self, Position target) {
//        return isInMeleeRange(self, target);
//    }
//    /**
//     * Performs an attack on the given target.
//     * If a critical hit occurs, the damage is doubled.
//     *
//     * @param target the combatant being attacked
//     */
//    @Override
//    public void attack(Combatant target) {
//        int damage = this.getPower();
//        if (isCriticalHit()) {
//            target.receiveDamage(getPower() * 2, this);
//            LogManager.addLog("Warrior attacked with Critical hit! for " + getPower() * 2 + " damage.");
//        } else {
//            target.receiveDamage(getPower(), this);
//            LogManager.addLog(this.getName() + " attacked the enemy for: " + damage + " damage.");
//        }
//    }
//
//    /**
//     * Returns whether this attack is a critical hit.
//     * There is a 10% chance for a critical hit.
//     *
//     * @return true if critical hit, false otherwise
//     */
//    @Override
//    public boolean isCriticalHit() {
//        double rand = Math.random();
//        return rand <= 0.1;
//    }
//
//    /**
//     * Processes incoming damage to the Warrior.
//     * The damage is reduced based on the Warrior's defense (up to a max of 60% reduction).
//     *
//     * @param amount the raw damage amount
//     * @param source the source of the damage
//     */
//    @Override
//    public void receiveDamage(int amount, Combatant source) {
//        int damage = (int) (amount * (1 - Math.min(0.6, defence / 200.0)));
//        this.setHealth(this.getHealth() - damage);
//        LogManager.addLog("Enemy attacked " + this.getName() + " for: " + amount + " damage, you received only " + damage);
//    }
//
//    /**
//     * Checks if this Warrior is equal to another object.
//     * Two Warriors are equal if they have the same name and position.
//     *
//     * @param obj the object to compare
//     * @return true if equal, false otherwise
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof Warrior other) {
//            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
//        }
//        return false;
//    }
//
//    /**
//     * Returns a string representation of the Warrior.
//     *
//     * @return a string containing the Warrior's name
//     */
//    @Override
//    public String toString() {
//        return "Warrior: " + this.getName();
//    }
//
//    // --- Fields ---
//
//    /**
//     * The Warrior's defense stat, which reduces incoming damage.
//     */
//    private final int defence;
//}
package game.characters;
import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.log.LogManager;
import game.map.Position;
import java.util.Random;

/**
 * The Warrior class represents a player character specialized in melee and physical combat.
 * Warriors have a chance to land critical hits and can reduce incoming damage using their defense stat.
 */
public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker {

    /**
     * Constructs a Warrior with the given name, position, and health.
     * The Warrior is assigned a random defense value between 0 and 119.
     * @param playerName the name of the Warrior
     * @param position   the initial position on the map
     * @param health     the initial health value
     */
    public Warrior(String playerName, Position position, int health) {
        super(playerName, position, health);
        Random random = new Random();
        this.defence = random.nextInt(120);
    }

    /**
     * Returns the symbol that represents this character on the map.
     * @return the string "Warrior"
     */
    @Override
    public String getDisplaySymbol() {
        return "Warrior";
    }

    /**
     * Engages in a close-range fight with a target if within melee range.
     * @param target the enemy combatant to fight
     */
    @Override
    public void fightClose(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            attack(target);
        } else {
            System.out.println("Target is out of melee range.");
        }
    }

    /**
     * Determines whether the target is within melee range (distance == 1).
     * @param self   the position of the Warrior
     * @param target the position of the target
     * @return true if within range, false otherwise
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }

    /**
     * Checks if the target is within range for a melee attack.
     * @param self   the position of the Warrior
     * @param target the position of the target
     * @return true if within range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return isInMeleeRange(self, target);
    }

    /**
     * Performs an attack on the given target.
     * If a critical hit occurs, the damage is doubled.
     * @param target the combatant being attacked
     */
    @Override
    public void attack(Combatant target) {
        int damage = this.getPower();
        if (isCriticalHit()) {
            target.receiveDamage(getPower() * 2, this);
            LogManager.addLog(this.getName() + " attacked with Critical hit! for " + getPower() * 2 + " damage.");
        } else {
            target.receiveDamage(getPower(), this);
            LogManager.addLog(this.getName() + " attacked the enemy for: " + damage + " damage.");
        }
    }

    /**
     * Returns whether this attack is a critical hit.
     * There is a 10% chance for a critical hit.
     * @return true if critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1;
    }

    /**
     * Processes incoming damage to the Warrior.
     * The damage is reduced based on the Warrior's defense (up to a max of 60% reduction).
     * @param amount the raw damage amount
     * @param source the source of the damage
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        int damage = (int) (amount * (1 - Math.min(0.6, defence / 200.0)));
        this.setHealth(this.getHealth() - damage);
        LogManager.addLog("Enemy attacked " + this.getName() + " for: " + amount + " damage, you received only " + damage);
    }

    /**
     * Checks if this Warrior is equal to another object.
     * Two Warriors are equal if they have the same name and position.
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Warrior other) {
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    /**
     * Returns a string representation of the Warrior.
     * @return a string containing the Warrior's name
     */
    @Override
    public String toString() {
        return "Warrior: " + this.getName();
    }

    // --- Fields ---

    /**
     * The Warrior's defense stat, which reduces incoming damage.
     */
    private final int defence;
}