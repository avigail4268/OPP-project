package game.combat;
import game.log.LogManager;
import game.map.Position;

public class CombatSystem {

    /**
     * Resolves a turn of combat between an attacker and a defender.
     * If in range, attacker attacks first, then defender counter-attacks if still alive.
     */
    public static void resolveCombat(Combatant attacker, Combatant defender) {
        if (!isInRange(attacker, defender)) {
            LogManager.addLog("Player is out of range!");
            return;
        }
        // ATTACKER'S TURN
        if (defender.tryEvade()) {
            LogManager.addLog("The enemy evaded the attack!");
        } else {
            attacker.attack(defender);
        }

        // DEFENDER'S TURN (only if still alive)
        if (!defender.isDead()) {
            if (attacker.tryEvade()) {
                LogManager.addLog("Player evaded the enemy's attack!");
            } else {
                defender.attack(attacker);
            }
        }

        // OUTCOME
        if (attacker.isDead()) {
            return;
        } else if (defender.isDead()) {
            LogManager.addLog("Enemy is dead! You won the fight!");
        }
    }

    /**
     * Checks if the attacker can reach the defender based on their combat type.
     */
    private static boolean isInRange(Combatant source, Combatant target) {
        Position sourcePos = source.getPosition();
        Position targetPos = target.getPosition();
        return source.isInRange(sourcePos, targetPos);
    }
}
