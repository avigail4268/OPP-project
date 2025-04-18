package game.combat;

import game.characters.Warrior;
import game.map.Position;

public class CombatSystem {
    public void resolveCombat(Combatant attacker, Combatant defender) {
        if (isInRange(attacker, defender)) {
            attacker.attack(defender);
            if (attacker.isDead()) {
                System.out.println("Game Over! you are dead!");
            } else {
                System.out.println("Enemy is dead! you won the fight!");
            }
        }
        else {
            System.out.println("You are not in range!");
        }
    }
    private boolean isInRange(Combatant source, Combatant target){
        Position sourceCurrentPos=source.getPosition();
        Position targetCurrentPOs=target.getPosition();
        if (source instanceof MeleeFighter mf){
            return mf.isInMeleeRange(sourceCurrentPos,targetCurrentPOs);
        }
        else if (source instanceof RangeFighter rf){
            return rf.isInRange(sourceCurrentPos,targetCurrentPOs);
        }
        return false;
    }
}
