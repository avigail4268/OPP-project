package game.combat;

import game.characters.Warrior;
import game.map.Position;

public class CombatSystem {
    public void resolveCombat(Combatant attacker, Combatant defender) {
        //TODO DELETE PRINT
        System.out.println("ATTACKING");
        if (isInRange(attacker, defender)) {
            //TODO DELETE PRINT
            System.out.println("DEFENDER");
            attacker.attack(defender);
            defender.attack(attacker);
            if (attacker.isDead()) {
                System.out.println("Game Over! you are dead!");
            } else {
                System.out.println("Enemy is dead! you won the fight!");
                System.out.println("DEAD?");
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
