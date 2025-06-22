package game.characters;
import game.combat.MagicElement;
import game.combat.*;
import game.log.LogManager;
import game.map.Position;

/**
 * Dragon represents an enemy character that can attack both physically and magically.
 * It implements PhysicalAttacker, MeleeFighter, and MagicAttacker interfaces, allowing it
 * to engage in both melee and magical combat with a special emphasis on its elemental power.
 */
public class Dragon extends Enemy implements PhysicalAttacker, MeleeFighter, MagicAttacker {

    /**
     * Constructs a new Dragon with a given position and health.
     * The Dragon's magic element is set to a default MagicElement.
     * @param pos the position of the Dragon
     * @param health the health of the Dragon
     */
    public Dragon(Position pos, int health) {
        super(pos, health);
        this.element = MagicElement.getElement();
    }

    /**
     * Creates a deep copy of the Dragon instance.
     * This method creates a new Dragon with the same position, health, and power,
     * and sets its magic element to the same as the original.
     * @return a new Dragon instance that is a copy of this one
     */
    public Enemy deepCopy() {
        Dragon dragon = new Dragon(this.getPosition(), this.getHealth());
        dragon.setPower(this.getPower());
        dragon.setElement(this.element);
        return dragon;
    }

    /**
     * Gets the display symbol representing the Dragon.
     * @return the string "DRAGON"
     */
    @Override
    public String getDisplaySymbol() {
        return "Dragon";
    }

    /**
     * Gets the magic element associated with the Dragon.
     * @return the Dragon's MagicElement
     */
    @Override
    public MagicElement getMagicElement() {
        return element;
    }

    /**
     * Calculates the magical damage dealt by the Dragon to the target.
     * The damage depends on whether the Dragon's element is stronger or weaker than the target's element.
     * @param target the target Combatant to calculate damage for
     */
    @Override
    public int calculateMagicDamage(Combatant target) {
        double magicPower = this.getPower() * 1.5;
        if (this.element.isElementStrongerThan(target.getMagicElement())) {
            target.receiveDamage((int)(magicPower * 1.2), this);
            LogManager.addLog("Dragon's element is stronger than the player's");
            LogManager.addLog("Dragon attacked by magic for: " + (int)(magicPower * 1.2) + " damage.");
        } else {
            target.receiveDamage((int)(magicPower * 0.8), this);
            LogManager.addLog("Dragon's element is stronger than the player's");
            LogManager.addLog("Dragon attacked by magic for: " + (int)(magicPower * 0.8) + " damage.");
        }
        return (int) magicPower;
    }

    /**
     * Casts a spell or attacks physically, depending on the target's magic element.
     * If the target has no magic element, the Dragon will attack physically; otherwise, it will use magic.
     * @param target the target Combatant to attack
     */
    @Override
    public void castSpell(Combatant target) {
        if (target.getMagicElement() == null) {
            double powerAttack = this.getPower() * 1.5;
            target.receiveDamage((int) powerAttack, this);
            LogManager.addLog("Dragon attacked physically for: " + (int) powerAttack + " damage.");
        } else {
            target.receiveDamage(calculateMagicDamage(target), this);
        }
    }

    /**
     * Determines if the Dragon's element is stronger than another MagicAttacker's element.
     * @param other the other MagicAttacker to compare
     * @return true if the Dragon's element is stronger, false otherwise
     */
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return element.isElementStrongerThan(other.getMagicElement());
    }

    /**
     * Executes a close-range attack on a target Combatant.
     * This is a physical attack, which may be a critical hit.
     * @param target the target Combatant to attack
     */
    @Override
    public void fightClose(Combatant target) {
        double powerAttack = this.getPower();
        if (isCriticalHit()) {
            target.receiveDamage((int) powerAttack * 2, this);
            LogManager.addLog("Dragon attacked with a Critical hit! for " + (int) powerAttack * 2 + " damage.");
        } else {
            target.receiveDamage((int) powerAttack, this);
            LogManager.addLog("Dragon attacked for " + (int) powerAttack + " damage.");
        }
    }

    /**
     * Checks if the target is within melee range of the Dragon.
     * @param self the Dragon's position
     * @param target the target's position
     * @return true if the target is within melee range, false otherwise
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) <= 1;
    }

    /**
     * Attacks a target, either physically (if in melee range) or using magic (if out of melee range).
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            fightClose(target);
        } else {
            castSpell(target);
        }
    }

    /**
     * Determines if the Dragon's attack is a critical hit.
     * The chance of a critical hit is 10%.
     * @return true if it's a critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        double rand = Math.random();
        return rand <= 0.1;
    }

    /**
     * Checks if the target is within range for a melee attack.
     * This method is used to determine if the Dragon can attack the target.
     * @param self the Dragon's position
     * @param target the target's position
     * @return true if the target is within melee range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return isInMeleeRange(self, target);
    }

    /**
     * Checks if two Dragon objects are equal.
     * They are considered equal if their position and magic element are the same.
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dragon other) {
            return this.getPosition() == other.getPosition() && this.getMagicElement() == other.getMagicElement();
        }
        return false;
    }

    /**
     * Gets a string representation of the Dragon.
     * @return a string in the format "DRAGON"
     */
    @Override
    public String toString() {
        return this.getDisplaySymbol();
    }

    /**
     * Sets the magic element of the Dragon.
     * This method allows changing the Dragon's magic element to a new one.
     * @param element the new MagicElement to set
     */
    protected void setElement(MagicElement element) {
        this.element = element;
    }

    // --- Fields ---
    private MagicElement element;
}
