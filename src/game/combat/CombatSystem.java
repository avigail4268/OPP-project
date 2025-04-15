package game.combat;

import game.characters.Warrior;

public class CombatSystem {
    void resolveCombat(Combatant attacker, Combatant defender) {
        if (attacker instanceof MeleeFighter && attacker.getPosition().distanceTo(defender.getPosition()) > 1) {
            Warrior warrior = new Warrior(attacker);
        } else if (attacker instanceof RangeFighter) {}
    }
}
