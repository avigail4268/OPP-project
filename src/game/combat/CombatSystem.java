package game.combat;

import game.characters.Warrior;
import game.map.Position;

public class CombatSystem {
    public void resolveCombat(Combatant attacker, Combatant defender) {
        if (isInRange(attacker, defender)) {
            if (defender.tryEvade()) {
                System.out.println("The enemy evaded the attack!");
            }else {
                attacker.attack(defender);
            }
            if (defender.tryEvade()) {
                System.out.println("You have been evaded from enemy attack!");
            }else {
                defender.attack(attacker);
            }
            if (attacker.isDead()) {
                System.out.println("Game Over! you are dead!");
            }else if (defender.isDead()) {
                System.out.println("Enemy is dead! you won the fight!");
            }else {
                System.out.println("You hit your enemy but not hard enough,he is still stand in is place.");
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
