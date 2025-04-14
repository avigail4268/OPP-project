package game.characters;

import game.combat.Combatant;
import game.core.GameEntity;
import game.map.Position;
import java.util.Random;

public abstract class AbstractCharacter implements Combatant, GameEntity{
    private Position position;
    private int health;
    private int power;
    private double evasionChance = 0.25;

    public AbstractCharacter(Position position) {
        setPosition(position);
        this.health = 100;
        this.power = new Random().nextInt(4,14);
    }
    @Override
    public void receiveDamage(int amount, Combatant source) {
        //TODO recheck
        if (tryEvade()) {
            System.out.println("You have evaded the attack!");
        }
        else
            this.health -= amount;
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
        if (newPos != null) {
            this.position = newPos;
            return true;
        }
        return false;
    }
    @Override
    public boolean setHealth(int health) {
        if (health > 0) {
            this.health = health;
            return true;
        }
        return false;
    }

    @Override
    public abstract String getDisplaySymbol();

    @Override
    //TODO something
    public abstract void setVisible(boolean visible);

    public int getHealth(){
        return health;
    }

}
