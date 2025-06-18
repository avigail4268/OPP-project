package game.decorator;

import game.characters.PlayerCharacter;
import game.combat.Combatant;
import game.core.GameEntity;
import game.map.Position;

public abstract class PlayerDecorator extends PlayerCharacter {
    /**
     * Constructs a new PlayerCharacter with a given name, position, and health.
     *
     * @param playerName the name of the player
     * @param position   the starting position of the player
     * @param health     the initial health of the player
     */

    private PlayerCharacter player ;

    public PlayerDecorator(PlayerCharacter player) {
        super(player);
    }

    @Override
    public String getDisplaySymbol() {
        return "";
    }

    @Override
    public GameEntity deepCopy() {
        return null;
    }

    @Override
    public void attack(Combatant target) {

    }

    @Override
    public boolean isInRange(Position self, Position target) {
        return false;
    }
}
