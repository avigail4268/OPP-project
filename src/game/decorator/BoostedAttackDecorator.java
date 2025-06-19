package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;

public class BoostedAttackDecorator extends PlayerDecorator {
    public BoostedAttackDecorator(PlayerCharacter player) {
        super(player);
    }

    @Override
    public void attack(Combatant target){

    }
}
