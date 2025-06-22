package game.decorator;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.log.LogManager;

/**
 * BoostedAttackDecorator is a decorator for PlayerCharacter that adds extra damage to attacks.
 * It extends the functionality of the PlayerCharacter by allowing it to deal additional damage
 * when attacking a target.
 */
public class BoostedAttackDecorator extends PlayerDecorator {

    /**
     * Constructs a BoostedAttackDecorator with the specified player character and extra damage.
     * @param player the PlayerCharacter to decorate
     * @param extraDamage the additional damage to be added to attacks
     */
    public BoostedAttackDecorator(PlayerCharacter player, int extraDamage) {
        super(player);
        this.extraDamage = extraDamage;
    }

    /**
     * Attacks the target Combatant, dealing the player's normal attack damage
     * and adding extra damage.
     */
    @Override
    public void attack(Combatant target) {
        super.attack(target);
        target.receiveDamage(extraDamage, this);
        LogManager.addLog("Boosted attack: +" + extraDamage + " damage dealt!");
    }

    // --- Fields ---
    private final int extraDamage;

}
