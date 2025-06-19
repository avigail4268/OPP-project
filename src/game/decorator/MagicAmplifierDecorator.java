package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.combat.MagicAttacker;

public class MagicAmplifierDecorator extends PlayerDecorator implements MagicAttacker {

    public MagicAmplifierDecorator(PlayerCharacter player) {
        super(player);
    }

//    @Override
//    public void attack(Combatant target) {
//        int targetHealthBefore = target.getHealth();
//        if (isMagicAttack()) {
//            int currentDamage = targetHealthBefore - target.getHealth();
//            if (currentDamage > 0) {
//                double amplificationRate = 0.04;
//                int bonusDamage = (int)(currentDamage * amplificationRate);
//                target.setHealth(target.getHealth() - bonusDamage);
//                System.out.println("[MagicAmplifierDecorator] תוספת נזק קסום: " + bonusDamage);
//            }
//        }
//    }



    private boolean isMagicAttack() {
        return getDecoratorPlayer().isMagicUser();
    }

    @Override
    public void calculateMagicDamage(Combatant target) {
         if ( getDecoratorPlayer() instanceof MagicAttacker magicAttacker)
         {
             magicAttacker.calculateMagicDamage(target);
         }
    }

    @Override
    public void castSpell(Combatant target) {
        target.receiveDamage(calculateMagicDamage(target) * 0.04, target);
    }

    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return false;
    }
}
