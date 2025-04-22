package game.characters;

import game.combat.Combatant;
import game.items.Treasure;
import game.map.GameMap;
import game.map.Position;

import java.util.Random;

public abstract class Enemy extends AbstractCharacter {
    private int loot;

    public Enemy(Position position,int health) {
        super(position,health);
        Random r = new Random();
        this.loot = r.nextInt(100,300);
    }
    public Treasure defeat () {
        if (this.isDead()) {
            Treasure treasure = new Treasure(this.getPosition(), false, "Treasure");
            return treasure;
        }
        return null;
    }
    public int getLoot() {
        return loot;
    }

}
