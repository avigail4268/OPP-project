package game.decorator;
import game.characters.PlayerCharacter;

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


    @Override
    public void update() {
    long now = System.currentTimeMillis();
    System.out.println("enter to the first level");
    if (now - lastRegenTime >= intervalMillis) {
        System.out.println("enter to the second level");
        int currentHealth = getDecoratorPlayer().getHealth();
        int maxHealth = getDecoratorPlayer().getMaxHealth();
        int newHealth = Math.min(currentHealth + regenAmount, maxHealth);
        getDecoratorPlayer().setHealth(newHealth);
        System.out.println("[HealthRegenDecorator] Regenerated " + regenAmount + " HP → " + newHealth + "/" + maxHealth);
        lastRegenTime = now;
    }
    super.update();
}
    @Override
    public int getHealth() {
        return getDecoratorPlayer().getHealth();
    }
    @Override
    public boolean setHealth(int health) {
        getDecoratorPlayer().setHealth(health);
        return true;
    }
}