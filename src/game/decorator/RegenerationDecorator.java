package game.decorator;

import game.characters.PlayerCharacter;

public class RegenerationDecorator extends PlayerDecorator {
    private final int regenerationAmount = 2; // Amount of health to regenerate per turn
    public RegenerationDecorator(PlayerCharacter player) {
        super(player);
    }

    private void regenerate() {
        if (getDecoratorPlayer().getHealth() < getDecoratorPlayer().getMaxHealth()) {
            getDecoratorPlayer().setHealth(getDecoratorPlayer().getHealth() + regenerationAmount);
            if (getDecoratorPlayer().getHealth() > getDecoratorPlayer().getMaxHealth()) {
                getDecoratorPlayer().setHealth(getDecoratorPlayer().getMaxHealth());
            }
        }
    }
}
