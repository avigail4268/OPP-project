package game.decorator;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.log.LogManager;

/**
 * MagicAmplifierDecorator is a decorator for PlayerCharacter that enhances magic attack damage by 4%.
 * It implements the MagicAttacker interface to provide magic attack functionality.
 */
public class MagicAmplifierDecorator extends PlayerDecorator implements MagicAttacker {

    /**
     * Constructs a new MagicAmplifierDecorator for the given PlayerCharacter.
     * @param player the PlayerCharacter to be decorated
     */
    public MagicAmplifierDecorator(PlayerCharacter player) {
        super(player);
    }

    /**
     * Calculates the magic damage dealt by the PlayerCharacter.
     * @return the calculated magic damage, amplified by 4%
     */
    @Override
    public int calculateMagicDamage(Combatant target) {
        int baseDamage = getMagicAttacker().calculateMagicDamage(target);
        return (int) (baseDamage * 1.04); // Amplify damage by 4%
    }

    /**
     * Casts a spell on the target Combatant, dealing magic damage.
     * Logs the action and prints a message to the console.
     * @param target the target Combatant to cast the spell on
     */
    @Override
    public void castSpell(Combatant target) {
        target.receiveDamage(this.calculateMagicDamage(target), this);
        LogManager.addLog("Magic Amplifier: spell cast with +4% damage.");
    }

    /**
     * Checks if the PlayerCharacter is stronger than another MagicAttacker in terms of magic element.
     * @param other the other MagicAttacker to compare against
     * @return true if this PlayerCharacter is stronger, false otherwise
     */
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return getMagicAttacker().isElementStrongerThan(other);
    }

    /**
     * Attacks a target Combatant using magic if the target is in range.
     * @return the original PlayerCharacter instance
     */
    @Override
    public void attack(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())) {
            castSpell(target);
        } else {
            LogManager.addLog("Magic Amplifier: Target is out of range for magic attack.");
        }
    }

    /**
     * Updates the state of the MagicAmplifierDecorator.
     */
    @Override
    public void update() {
        getDecoratorPlayer().update();
        // No additional update logic needed for MagicAmplifierDecorator
    }

    /**
     * Gets the underlying MagicAttacker from the PlayerCharacter.
     * @return the MagicAttacker instance
     * @throws IllegalStateException if the PlayerCharacter does not implement MagicAttacker
     */
    private MagicAttacker getMagicAttacker() {
        PlayerCharacter original = unwrap();
        if (original instanceof MagicAttacker magicAttacker) {
            return magicAttacker;
        }
        throw new IllegalStateException("MagicAmplifierDecorator must wrap a MagicAttacker!");
    }
    @Override
    public int getHealth() {
        return getDecoratorPlayer().getHealth();
    }
    @Override
    public boolean setHealth(int health) {
        getDecoratorPlayer().setHealth(health);
        return true;
    }
}
