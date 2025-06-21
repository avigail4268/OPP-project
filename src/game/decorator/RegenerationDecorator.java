package game.decorator;

import game.characters.PlayerCharacter;
import game.gui.GameFrame;
import game.log.LogManager;

import java.awt.*;

/**
 * RegenerationDecorator is a decorator for PlayerCharacter that provides health regeneration functionality.
 * It extends PlayerDecorator and implements the update method to handle health regeneration over time.
 */
public class RegenerationDecorator extends PlayerDecorator {

    /**
     * Constructs a new RegenerationDecorator for the given PlayerCharacter.
     * @param player the PlayerCharacter to be decorated
     * @param regenAmount the amount of health to regenerate each interval
     * @param intervalSeconds the time interval in seconds between regenerations
     */
    public RegenerationDecorator(PlayerCharacter player, int regenAmount, int intervalSeconds) {
        super(player);
        this.regenAmount = regenAmount;
        this.intervalMillis = intervalSeconds * 1000L;
        this.lastRegenTime = System.currentTimeMillis();
    }

    /**
     * Updates the PlayerCharacter's health based on the regeneration logic.
     * This method checks if enough time has passed since the last regeneration and applies the health increase.
     */
    public void update() {
        long now = System.currentTimeMillis();
        getDecoratorPlayer().update();
        if (now - lastRegenTime >= intervalMillis) {
            int currentHealth = getDecoratorPlayer().getHealth();
            int maxHealth = getDecoratorPlayer().getMaxHealth();

            if (currentHealth < maxHealth) {
                int newHealth = Math.min(currentHealth + regenAmount, maxHealth);
                this.setHealth(newHealth);
                LogManager.addLog("[HealthRegenDecorator] Regenerated " + regenAmount + " HP → " + newHealth + "/" + maxHealth);
            }
            lastRegenTime = now;
        }
    }

    // --- Fields ---
    private final int regenAmount;
    private final long intervalMillis;
    private long lastRegenTime;
}




