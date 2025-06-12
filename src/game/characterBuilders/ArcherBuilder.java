package game.characterBuilders;

import game.characters.AbstractCharacter;
import game.characters.Archer;
import game.map.Position;

public class ArcherBuilder implements CharacterBuilder {
    private Archer archer;
    public ArcherBuilder() {}

    @Override
    public void buildPower(int power) {
        archer.setPower(power);
    }

    @Override
    public void buildHealth(int health) {
         archer.setHealth(health);
    }

    @Override
    public AbstractCharacter getCharacter() {
        return archer;
    }

    @Override
    public void build(String name, Position pos) {
        archer = new Archer(name,pos,100);
    }

}
