package game.decorator;
import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.core.GameEntity;
import game.map.Position;

public abstract class PlayerDecorator extends PlayerCharacter {

    private final PlayerCharacter decoratorPlayer ;

    public PlayerDecorator(PlayerCharacter player) {
        super(player);
        decoratorPlayer = player;
    }

    @Override
    public String getDisplaySymbol() {
        return decoratorPlayer.getDisplaySymbol();
    }

    @Override
    public GameEntity deepCopy() {
        return decoratorPlayer.deepCopy();
    }

    @Override
    public void attack(Combatant target) {
        decoratorPlayer.attack(target);
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        return decoratorPlayer.isInRange(self, target);
    }

    public PlayerCharacter getDecoratorPlayer() {
        return decoratorPlayer;
    }

    @Override
    public void update() {
        getDecoratorPlayer().update();
    }

    @Override
    public boolean setHealth(int health) {
        getDecoratorPlayer().setHealth(health);
        return true;
    }

    @Override
    public int getHealth() {
        return getDecoratorPlayer().getHealth();
    }
    @Override
    public int getMaxHealth() {
        return decoratorPlayer.getMaxHealth();
    }
}
