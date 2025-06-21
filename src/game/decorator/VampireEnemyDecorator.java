package game.decorator;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.log.LogManager;

/**
 * VampireEnemyDecorator is a decorator for Enemy that adds vampiric abilities to drain health from the player.
 * It extends the EnemyDecorator and overrides the attack method to implement the health draining logic.
 */
public class VampireEnemyDecorator extends EnemyDecorator {

    /**
     * Constructs a new VampireEnemyDecorator for the given Enemy.
     * @param enemy the Enemy to be decorated
     */
    public VampireEnemyDecorator(Enemy enemy) {
        super(enemy);
    }

    /**
     * Attacks the target Combatant and drains health from the player if the target is a PlayerCharacter.
     * The enemy gains 1% of the player's current health, up to its maximum health.
     * @param target the target Combatant to attack
     */
    @Override
    public void attack(Combatant target) {
        super.attack(target);

        if (target instanceof PlayerCharacter player) {
            int stolenAmount = Math.max(1, (int)(player.getHealth() * 0.01));
            int newHealth = Math.min(getHealth() + stolenAmount, getMaxHealth());

            this.setHealth(newHealth);
            LogManager.addLog("[VampireEnemy] Drained " + stolenAmount + " HP from player → Enemy health: " + newHealth + "/" + getMaxHealth());
        }
    }
}


