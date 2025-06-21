package game.characters;
import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.log.LogManager;
import game.map.Position;
import java.util.Random;

/**
 * Goblin represents an enemy character that is a physical attacker with a melee fighting style.
 * It extends the Enemy class and has agility as a key feature affecting evasion.
 */
public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter {

    /**
     * Constructs a new Goblin with the given position and health.
     * A random agility value between 0 and 80 is assigned to the goblin.
     * @param pos the position of the goblin
     * @param health the health of the goblin
     */
    public Goblin(Position pos, int health) {
        super(pos, health);
        agility = new Random().nextInt(80); // Random agility between 0 and 80
    }

    /**
     * Creates a deep copy of the Goblin.
     * @return a new Goblin instance with the same position, health, and agility
     */
    public Enemy deepCopy() {
        Goblin goblin = new Goblin(this.getPosition(), this.getHealth());
        goblin.setAgility(this.agility);
        return goblin;
    }

    /**
     * Returns the symbol representing the Goblin.
     * @return the string "GOBLIN"
     */
    @Override
    public String getDisplaySymbol() {
        return "Goblin";
    }

    /**
     * Performs a melee attack on the target if the target is in range.
     * @param target the target Combatant to attack
     */
    @Override
    public void fightClose(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            this.attack(target);
        }
    }

    /**
     * Checks if the target is within melee range (distance <= 1).
     * @param self the position of the Goblin
     * @param target the position of the target
     * @return true if the target is within melee range, false otherwise
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }

    /**
     * Attempts to evade an attack based on the Goblin's agility.
     * The chance of evading an attack is calculated as the agility divided by 100,
     * and it is capped at 0.8. If the evade chance is less than or equal to 0.25,
     * the Goblin successfully evades.
     * @return true if the Goblin successfully evades the attack, false otherwise
     */
    @Override
    public boolean tryEvade() {
        double evadeChance = Math.min(0.8, agility / 100.0);
        return evadeChance <= 0.25;
    }

    /**
     * Attacks the target with a physical attack. If a critical hit is rolled (10% chance),
     * the damage is doubled.
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        if (isCriticalHit()) {
            target.receiveDamage(getPower() * 2, this);
            LogManager.addLog("Goblin attack with Critical hit! for: " + getPower() * 2 + " damage.");
        } else {
            target.receiveDamage(getPower(), this);
            LogManager.addLog("Goblin attacked for: " + getPower() + " damage.");
        }
    }

    /**
     * Determines if the Goblin's attack is a critical hit.
     * A critical hit occurs if a random number between 0 and 1 is less than or equal to 0.1.
     * @return true if the attack is a critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1;
    }

    /**
     * Checks if the Goblin is within range to attack the target.
     * The Goblin can attack if the target is within melee range.
     * @param self the position of the Goblin
     * @param target the position of the target
     * @return true if the Goblin can attack, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return isInMeleeRange(self, target);
    }

    /**
     * Compares this Goblin to another object to check for equality.
     * Goblins are considered equal if they have the same position and agility.
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Goblin other) {
            return this.getPosition() == other.getPosition() && this.getAgility() == other.getAgility();
        }
        return false;
    }

    /**
     * Returns a string representation of the Goblin.
     * @return the string representation of the Goblin
     */
    @Override
    public String toString() {
        return this.getDisplaySymbol();
    }

    /**
     * Gets the agility of the Goblin.
     * @return the agility of the Goblin
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Sets the agility of the Goblin.
     * @param agility the new agility value to set
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }


    // --- Fields ---
    private int agility; // Agility of the Goblin that affects evasion
}
