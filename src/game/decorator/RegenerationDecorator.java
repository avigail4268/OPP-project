//package game.decorator;
//
//import game.characters.PlayerCharacter;
//
//public class RegenerationDecorator extends PlayerDecorator {
//    private final int regenerationAmount = 2; // Amount of health to regenerate per turn
//    public RegenerationDecorator(PlayerCharacter player) {
//        super(player);
//    }
//
//    private void regenerate() {
//        if (getDecoratorPlayer().getHealth() < getDecoratorPlayer().getMaxHealth()) {
//            getDecoratorPlayer().setHealth(getDecoratorPlayer().getHealth() + regenerationAmount);
//            if (getDecoratorPlayer().getHealth() > getDecoratorPlayer().getMaxHealth()) {
//                getDecoratorPlayer().setHealth(getDecoratorPlayer().getMaxHealth());
//            }
//        }
//    }
//}
package game.decorator;

import game.characters.PlayerCharacter;
import game.decorator.PlayerDecorator;

public class RegenerationDecorator extends PlayerDecorator {

    private final int regenAmount;
    private final long intervalMillis;
    private long lastRegenTime;

    public RegenerationDecorator(PlayerCharacter player, int regenAmount, int intervalSeconds) {
        super(player);
        this.regenAmount = regenAmount;
        this.intervalMillis = intervalSeconds * 1000L;
        this.lastRegenTime = System.currentTimeMillis();
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastRegenTime >= intervalMillis) {
            int currentHealth = getDecoratorPlayer().getHealth();
            int maxHealth = getDecoratorPlayer().getMaxHealth();

            if (currentHealth < maxHealth) {
                int newHealth = Math.min(currentHealth + regenAmount, maxHealth);
                getDecoratorPlayer().setHealth(newHealth);
                System.out.println("[HealthRegenDecorator] Regenerated " + regenAmount + " HP → " + newHealth + "/" + maxHealth);
            }

            lastRegenTime = now;
        }
    }
}