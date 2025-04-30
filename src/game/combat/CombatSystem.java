package game.combat;
import game.map.Position;

public class CombatSystem {

    /**
     * Resolves a turn of combat between an attacker and a defender.
     * If in range, attacker attacks first, then defender counter-attacks if still alive.
     */
    public static void resolveCombat(Combatant attacker, Combatant defender) {
        if (!isInRange(attacker, defender)) {
            System.out.println("You are not in range!");
            return;
        }
        // ATTACKER'S TURN
        if (defender.tryEvade()) {
            System.out.println("The enemy evaded the attack!");
        } else {
            System.out.println("try attack");
            attacker.attack(defender);
        }

        // DEFENDER'S TURN (only if still alive)
        if (!defender.isDead()) {
            if (attacker.tryEvade()) {
                System.out.println("You evaded the enemy's attack!");
            } else {
                System.out.println("enemy try attack");
                defender.attack(attacker);
            }
        }

        // OUTCOME
        if (attacker.isDead()) {
            return;
        } else if (defender.isDead()) {
            System.out.println("Enemy is dead! You won the fight!");
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
