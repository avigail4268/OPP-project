package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;

public class MagicAmplifierDecorator extends PlayerDecorator {

    public MagicAmplifierDecorator(PlayerCharacter player) {
        super(player);
    }

    @Override
    public void attack(Combatant target) {
        int targetHealthBefore = target.getHealth();
        super.attack(target);
        if (isMagicAttack()) {
            int damageDealt = targetHealthBefore - target.getHealth();
            if (damageDealt > 0) {
                double amplificationRate = 0.04;
                int bonusDamage = (int)(damageDealt * amplificationRate);
                target.setHealth(target.getHealth() - bonusDamage);
                System.out.println("[MagicAmplifierDecorator] תוספת נזק קסום: " + bonusDamage);
            }
        }
    }

    private boolean isMagicAttack() {
        return getDecoratorPlayer().isMagicUser();
    }
}
