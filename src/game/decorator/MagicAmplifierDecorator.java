package game.decorator;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.log.LogManager;

public class MagicAmplifierDecorator extends PlayerDecorator implements MagicAttacker {

    public MagicAmplifierDecorator(PlayerCharacter player) {
        super(player);
    }

    private boolean isMagicAttack() {
        return getDecoratorPlayer().isMagicUser();
    }

    @Override
    public int calculateMagicDamage(Combatant target) {
         if ( getDecoratorPlayer() instanceof MagicAttacker magicAttacker)
         {
            return magicAttacker.calculateMagicDamage(target);
         }
         return 0;
    }

    @Override
    public void castSpell(Combatant target) {
        target.receiveDamage((int) (calculateMagicDamage(target)*1.04), this);
        LogManager.addLog("Magic Amplifier: Spell cast with amplified damage.");
    }

    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return getDecoratorPlayer().getMagicElement().isElementStrongerThan(other.getMagicElement());
    }
}
