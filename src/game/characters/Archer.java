package game.characters;
import game.combat.Combatant;
import game.combat.PhysicalAttacker;
import game.combat.RangeFighter;
import game.log.LogManager;
import game.map.Position;
import java.util.Random;

/**
 * Archer represents a player character specialized in ranged combat.
 * It implements RangeFighter and PhysicalAttacker interfaces, allowing the Archer
 * to attack from a distance with a chance for critical hits and the ability to evade attacks.
 */
public class Archer extends PlayerCharacter implements RangeFighter, PhysicalAttacker {

    /**
     * Constructs a new Archer with the given name, position, and health.
     * The Archer is assigned a random accuracy value between 0.8 and 1.0.
     * @param name the name of the Archer
     * @param pos the position of the Archer
     * @param health the health of the Archer
     */
    public Archer(String name, Position pos, int health) {
        super(name, pos, health);
        Random rand = new Random();
        accuracy = rand.nextDouble(0.8);
    }

    /**
     * Sets the accuracy of the Archer.
     */
    public void setAccuracy (double accuracy) {
        this.accuracy = accuracy;

    }

    /**
     * Creates a deep copy of the Archer character.
     * @return a new Archer instance with the same properties as this one
     */
    @Override
    public PlayerCharacter deepCopy() {
        Archer archer = new Archer(this.getName(), this.getPosition(),this.getHealth());
        archer.setAccuracy(this.accuracy);
        archer.setPower(this.getPower());
        archer.setInventory(this.getInventory());
        archer.setTreasurePoints(this.getTreasurePoints());
        return archer;
    }

    /**
     * Returns the symbol representing the Archer.
     * @return the string "Archer"
     */
    @Override
    public String getDisplaySymbol() {
        return "Archer";
    }

    /**
     * Attacks the target Combatant with a ranged attack.
     * If the attack is a critical hit, the damage is doubled.
     * Logs the attack details using LogManager.
     * @param target the Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        int damage = this.getPower();
        if (isCriticalHit()) {
            target.receiveDamage(getPower() * 2, this);
            LogManager.addLog("Archer attacked with Critical hit! for " + getPower() * 2 + " damage.");
        } else {
            target.receiveDamage(getPower(), this);
            LogManager.addLog(this.getName() + " attacked the enemy for " + damage + " damage.");
        }
    }

    /**
     * Determines if the Archer's attack is a critical hit.
     * The chance of a critical hit is 10%.
     * @return true if it's a critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1;
    }

    /**
     * Executes a ranged attack on the target if they are within range.
     * @param target the Combatant to attack
     */
    @Override
    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            attack(target);
        }
    }

    /**
     * Checks if the target is within the attack range of the Archer.
     * @param self the Archer's position
     * @param target the target's position
     * @return true if the target is within range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }

    /**
     * Gets the range of the Archer's attacks.
     * The Archer can attack up to 2 units away.
     * @return the attack range (2)
     */
    @Override
    public int getRange() {
        return 2;
    }

    /**
     * Checks if two Archer objects are equal.
     * They are considered equal if their name and position are the same.
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Archer other) {
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    /**
     * Gets a string representation of the Archer.
     * @return a string in the format "Archer: [name]"
     */
    @Override
    public String toString() {
        return "Archer: " + this.getName();
    }

    // --- Fields ---
    private double accuracy;
}
