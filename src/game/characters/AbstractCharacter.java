package game.characters;

import game.combat.Combatant;
import game.core.GameEntity;
import game.map.Position;

import java.util.Random;

public abstract class  AbstractCharacter implements Combatant, GameEntity{
    private Position position;
    private int health;
    private int power;
    private final double evasionChance = 0.25;

    public AbstractCharacter(Position position) {
        setPosition(position);
        health = 100;
        power = new Random().nextInt(4,14);
    }
    @Override
    public void receiveDamage(int amount, Combatant source) {
        //subtract amount from health
        if (tryEvade()) return;
        //TODO
    }

    @Override
    public void heal(int amount) {
        this.health += amount;
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public boolean tryEvade() {
        double evadeChance = Math.random();
        if (evadeChance < this.evasionChance) {
            return true;
        }
        return false;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean setPosition(Position newPos) {
        // TODD
        return false;
    }
    @Override
    public abstract boolean setHealth(int health);

    @Override
    public abstract String getDisplaySymbol();

    @Override
    public abstract boolean setVisible(boolean visible);
}
