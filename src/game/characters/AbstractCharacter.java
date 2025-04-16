package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.core.GameEntity;
import game.map.Position;
import java.util.Random;

public abstract class AbstractCharacter implements Combatant, GameEntity{
    private Position position;
    private int health;
    private int power;
    private boolean visible;
    private double evasionChance = 0.25;

    public AbstractCharacter(Position position,int health) {
        setPosition(position);
        this.health = health;
        this.power = new Random().nextInt(4,14);
        this.visible = true;
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
        return health == 0;
    }

    @Override
    public int getPower() {
        return power;
    }

    public boolean setPower(int amount) {
        if (amount > 0){
            this.power += amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean tryEvade() {
        double evadeChance = Math.random();
        return evadeChance < this.evasionChance;
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
    public double getEvasionChance () {
        return evasionChance;
    }
    public void setVisible(boolean visible){
        this.visible = visible;
    }
    @Override
    public int getHealth(){
        return health;
    }
    @Override
    public abstract String getDisplaySymbol();

    public abstract MagicElement getMagicElement();

    //i dont think its sopposed to be absract cause only mage has magic element and we cant
    //use it in other classes like warrior or archer

}
