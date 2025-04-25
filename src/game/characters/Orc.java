package game.characters;
import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;
import java.util.Random;

/**
 * Orc represents an enemy character capable of receiving damage from both physical and magical attacks.
 * The Orc has resistance to magic damage and can attack in melee range.
 */
public class Orc extends Enemy implements MeleeFighter, PhysicalAttacker {

    /**
     * Constructs a new Orc with the given position and health.
     * The Orc is assigned a random resistance to magic attacks.
     *
     * @param pos the position of the Orc
     * @param health the health of the Orc
     */
    public Orc(Position pos, int health) {
        super(pos, health);
        resistance = new Random().nextDouble() * 0.5; // Resistance value between 0 and 0.5
    }

    /**
     * Receives damage from an attack. If the attack is magical, the damage is reduced based on the Orc's resistance.
     * If the attack is physical, the damage is applied normally.
     *
     * @param amount the amount of damage received
     * @param source the source of the attack
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        if (source.getMagicElement() != null) {
            // Calculate reduced magic damage based on resistance
            double magicDamage = amount * (1 - resistance);
            super.receiveDamage((int) magicDamage, source);
            System.out.println("Orc received " + (int) magicDamage + " damage from magic attack.");
        } else {
            super.receiveDamage(amount, source);
            System.out.println("Orc received " + amount + " damage from physical attack.");
        }
    }

    /**
     * Returns the symbol representing the Orc.
     *
     * @return the string "ORC"
     */
    @Override
    public String getDisplaySymbol() {
        return "ORC";
    }
    /**
     * Executes a close-range melee attack on the target.
     *
     * @param target the target Combatant to attack
     */
    @Override
    public void fightClose(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            this.attack(target);
        }
    }

    /**
     * Checks if the target is within melee range.
     *
     * @param self the position of the Orc
     * @param target the position of the target
     * @return true if the target is within melee range, false otherwise
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }
    /**
     * Returns the range of the Orc's attack.
     *
     * @return the range of the Orc's attack (1)
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return isInMeleeRange(self, target);
    }
    /**
     * Performs an attack on the target. If the attack is a critical hit, the damage is doubled.
     * The damage is applied based on the Orc's power.
     *
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            if (isCriticalHit()) {
                target.receiveDamage(getPower() * 2, this);
                System.out.println("The orc attacked back with Critical hit! for: " + getPower() * 2 + " damage.");
            } else {
                target.receiveDamage(getPower(), this);
                System.out.println("The orc attacked back for: " + getPower() + " damage.");
            }
        } else {
            System.out.println(target + " is out of melee range.");
        }
    }

    /**
     * Determines if the attack is a critical hit. The chance of a critical hit is 10%.
     *
     * @return true if the attack is a critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1; // 10% chance of a critical hit
    }

    /**
     * Compares this Orc to another object for equality.
     * Orcs are considered equal if they have the same position and resistance.
     *
     * @param obj the object to compare
     * @return true if the Orcs are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Orc other) {
            return this.getPosition() == other.getPosition() && this.getResistance() == other.getResistance();
        }
        return false;
    }

    /**
     * Returns a string representation of the Orc.
     *
     * @return the string representation of the Orc, including its display symbol
     */
    @Override
    public String toString() {
        return this.getDisplaySymbol();
    }
    /**
     * Returns the Orc's resistance to magic damage.
     *
     * @return the Orc's magic damage resistance
     */
    protected double getResistance() {
        return resistance;
    }

    // --- Fields ---
    /**
     * The Orc's resistance to magic damage.
     */
    private final double resistance;
}
