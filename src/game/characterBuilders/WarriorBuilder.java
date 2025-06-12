package game.characterBuilders;

import game.characters.*;
import game.map.Position;

public class WarriorBuilder implements CharacterBuilder {

    private Warrior warrior;

    public WarriorBuilder() {

    }

    public void buildHealth(int health) {
        warrior.setHealth(100 + health);
    }

    public AbstractCharacter getCharacter() {
        return warrior;
    }

    @Override
    public void buildPower(int power) {
        int oldPower = warrior.getPower();
        warrior.setPower(oldPower + power);
    }

    public void buildDefence(int defence) {
        int oldDefence = warrior.getDefence();
        warrior.setDefence(oldDefence + defence);
    }

    public void build(String name , Position pos ) {
        warrior = new Warrior(name, pos , 100);

    }
}