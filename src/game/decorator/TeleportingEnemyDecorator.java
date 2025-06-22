package game.decorator;
import game.characters.Enemy;
import game.combat.Combatant;
import game.log.LogManager;
import game.map.GameMap;
import game.map.Position;

/**
 * TeleportingEnemyDecorator is a decorator for Enemy that adds teleportation functionality when health drops below a certain threshold.
 * It extends the EnemyDecorator and overrides the receiveDamage method to implement the teleportation logic.
 */
public class TeleportingEnemyDecorator extends EnemyDecorator {

    /**
     * Constructs a new TeleportingEnemyDecorator for the given Enemy.
     * @param enemy the Enemy to be decorated
     */
    public TeleportingEnemyDecorator(Enemy enemy) {
        super(enemy);
    }

    /**
     * Receives damage and checks if the enemy's health is below a certain threshold.
     * @param amount the amount of damage to receive
     * @param source the Combatant that is dealing the damage
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        super.receiveDamage(amount, source);

        if (!hasTeleported && getHealth() < getMaxHealth() * TELEPORT_THRESHOLD) {
            GameMap map = GameMap.getInstance();
            Position newPos = map.getRandomEmptyPosition();
            if (newPos != null) {
                getDecoratorEnemy().setPosition(newPos);
                hasTeleported = true;
                LogManager.addLog("[TeleportingEnemyDecorator] Enemy teleported to " + newPos);
            }
        }
    }

    // --- Fields ---

    private static final double TELEPORT_THRESHOLD = 0.30;
    private volatile boolean hasTeleported = false;
}
