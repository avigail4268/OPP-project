package game.decorator;

import game.characters.PlayerCharacter;

public class ShieldedPlayerDecorator extends PlayerDecorator {

    public ShieldedPlayerDecorator(PlayerCharacter player) {
        super(player);
    }
}
