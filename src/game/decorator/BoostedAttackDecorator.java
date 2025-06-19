package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;

public class BoostedAttackDecorator extends PlayerDecorator {

    private final int extraDamage;

    public BoostedAttackDecorator(PlayerCharacter player, int extraDamage) {
        super(player);
        this.extraDamage = extraDamage;
    }

    @Override
    public void attack(Combatant target) {
        int currentDamage = target.getHealth();
        if (currentDamage > 0) {
            target.receiveDamage(getDecoratorPlayer().getPower() + extraDamage , this);
            System.out.println("[BoostedAttackDecorator] Bonus damage: " + extraDamage);
        }
    }
}