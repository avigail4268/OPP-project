package game.characters;
import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.MagicElement;
import game.combat.RangeFighter;
import game.core.GameEntity;
import game.log.LogManager;
import game.map.Position;

/**
 * Mage represents a player character that is capable of using magic attacks from a range.
 * It extends the PlayerCharacter class and implements both RangeFighter and MagicAttacker interfaces.
 */
public class Mage extends PlayerCharacter implements RangeFighter, MagicAttacker {

    /**
     * Constructs a new Mage with the given name, position, and health.
     * The Mage is assigned a magic element.
     * @param name the name of the Mage
     * @param pos the position of the Mage
     * @param health the health of the Mage
     */
    public Mage(String name, Position pos, int health) {
        super(name, pos, health);
        this.element = MagicElement.getElement(); // Assign a magic element to the Mage
    }

    @Override
    public PlayerCharacter deepCopy() {
        Mage mage = new Mage(this.getName(), this.getPosition(), this.getHealth());
        mage.setElement(this.element);
        mage.setPower(this.getPower());
        mage.setInventory(this.getInventory().deepCopy());
        mage.setTreasurePoints(this.getTreasurePoints());
        return mage;
    }


    /**
     * Returns the symbol representing the Mage.
     * @return the string "MAGE"
     */
    @Override
    public String getDisplaySymbol() {
        return "Mage";
    }

    /**
     * Returns the magic element of the Mage.
     * @return the Mage's magic element
     */
    @Override
    public MagicElement getMagicElement() {
        return element;
    }

    /**
     * Attacks the target by casting a spell. The magic damage is calculated based on the Mage's element
     * and the target's element, and the target receives the corresponding damage.
     * @param element the target Combatant to attack
     */

    public void setElement(MagicElement element) {
        this.element = element;
    }

    @Override
    public void attack(Combatant target) {
        fightRanged(target);
    }

    /**
     * Calculates the magic damage dealt to the target.
     * The damage is influenced by the Mage's power and the comparison between the Mage's
     * element and the target's element.
     * @param target the target Combatant to calculate damage for
     */
    @Override
    public int calculateMagicDamage(Combatant target) {
        double totalDamage = this.getPower() * 1.5;
        MagicElement targetElement = target.getMagicElement();

        // Element comparison to determine damage modifiers
        if (targetElement != null) {
            if (this.element.isElementStrongerThan(targetElement)) {
                totalDamage *= 1.2; // Element stronger than the target's element
                LogManager.addLog("Mage attack by magic, his element is stronger than the enemy's");
            } else if (targetElement.isElementStrongerThan(this.element)) {
                totalDamage *= 0.8; // Element weaker than the target's element
                LogManager.addLog("Mage attack by magic, his element is weaker than the enemy's");
            }
        }
        LogManager.addLog(this.getName() + " attacked the enemy for: " + (int) totalDamage + " damage.");
        return (int) totalDamage;
    }

    /**
     * Casts a spell on the target, which is essentially performing the magic attack.
     * @param target the target Combatant to cast the spell on
     */
    @Override
    public void castSpell(Combatant target) {
        target.receiveDamage(calculateMagicDamage(target), this);
    }

    /**
     * Checks if the Mage's magic element is stronger than the other magic attacker's element.
     * @param other the other MagicAttacker to compare elements with
     * @return true if the Mage's magic element is stronger, false otherwise
     */
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return element.isElementStrongerThan(other.getMagicElement());
    }

    /**
     * Attempts to attack a target from a ranged distance. The Mage will cast a spell
     * if the target is within the Mage's range.
     * @param target the target Combatant to attack
     */
    @Override
    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            castSpell(target);
        }
    }

    /**
     * Returns the range of the Mage's attack.
     * @return the range of the Mage's attack (2)
     */
    @Override
    public int getRange() {
        return 2; // Mage has a range of 2
    }

    /**
     * Checks if the target is within the Mage's attack range.
     * @param self the position of the Mage
     * @param target the position of the target
     * @return true if the target is within range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }

    /**
     * Compares this Mage to another object to check for equality.
     * Mages are considered equal if they have the same name and position.
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Mage other) {
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    /**
     * Returns a string representation of the Mage.
     * @return the string representation of the Mage player character
     */
    @Override
    public String toString() {
        return "Mage: " + this.getName();
    }

    @Override
    public boolean isMagicUser(){return true;}

    // --- Fields ---
    /**
     * The magic element of the Mage.
     * This field is used to determine the type of magic attacks the Mage can perform.
     */
    private MagicElement element; // Mage's magic element
}
