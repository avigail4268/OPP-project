package game.characterBuilders;

import game.characters.*;
import game.map.Position;

public class WarriorBuilder implements CharacterBuilder {

    private Warrior warrior;

    public WarriorBuilder( String name , Position pos) {
        warrior = new Warrior( name , pos, 100);
    }

    public void buildHealth(int health) {
        warrior.setHealth(health);
    }

    @Override
    public void randomizeStats() {
        // לא חובה אם הכל מגיע מה-GUI
    }
    @Override
    public void buildPower(int power) {
        warrior.setPower(power);
    }

    public void buildDefence(int defence) {
        warrior.setDefence(defence);
    }

    @Override
    public Warrior build() {

        };
    }
}