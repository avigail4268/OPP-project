package game.characters;

import game.items.Treasure;
import game.map.Position;

import java.util.Random;

public abstract class Enemy extends AbstractCharacter {
    private int loot;

    public Enemy(Position position,int health) {
        super(position,health);
        Random r = new Random();
        this.loot = r.nextInt(100,300);
    }

    public void defeat(){
        if (this.getHealth() <= 0){
            Treasure treasure = new Treasure(this.getPosition(),false,"Treasure");

        }
    }
}
