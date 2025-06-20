package game.decorator;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.log.LogManager;

public class BoostedAttackDecorator extends PlayerDecorator {

    private final int extraDamage;

    public BoostedAttackDecorator(PlayerCharacter player, int extraDamage) {
        super(player);
        this.extraDamage = extraDamage;
    }

    @Override
    public void attack(Combatant target) {
        getDecoratorPlayer().attack(target);
        target.receiveDamage(extraDamage, this);
        LogManager.addLog("Extra damage");
    }
}