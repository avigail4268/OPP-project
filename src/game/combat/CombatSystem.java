package game.combat;
import game.map.Position;

public class CombatSystem {

    /**
     * Resolves a turn of combat between an attacker and a defender.
     * If in range, attacker attacks first, then defender counter-attacks if still alive.
     */
    public void resolveCombat(Combatant attacker, Combatant defender) {
        if (!isInRange(attacker, defender)) {
            System.out.println("You are not in range!");
            return;
        }
        // ATTACKER'S TURN
        if (defender.tryEvade()) {
            System.out.println("The enemy evaded the attack!");
        } else {
            attacker.attack(defender);
        }

        // DEFENDER'S TURN (only if still alive)
        if (!defender.isDead()) {
            if (attacker.tryEvade()) {
                System.out.println("You evaded the enemy's attack!");
            } else {
                defender.attack(attacker);
            }
        }

        // OUTCOME
        if (attacker.isDead()) {
            System.out.println("Game Over! You are dead!");
        } else if (defender.isDead()) {
            System.out.println("Enemy is dead! You won the fight!");
        }
    }

    /**
     * Checks if the attacker can reach the defender based on their combat type.
     */
    private boolean isInRange(Combatant source, Combatant target) {
        Position sourcePos = source.getPosition();
        Position targetPos = target.getPosition();
        if (source.isInRange(sourcePos, targetPos)) {
            return true;
        } else {
            System.out.println("Target is out of range.");
        }
        return false;
    }
}
