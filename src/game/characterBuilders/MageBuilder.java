package game.characterBuilders;
import game.characters.AbstractCharacter;
import game.characters.Mage;
import game.combat.MagicElement;
import game.map.Position;

public class MageBuilder implements CharacterBuilder {
    private Mage mage;

    public MageBuilder() {
    }

    @Override
    public void buildPower(int power) {
        int oldPower = mage.getPower();
        mage.setPower(oldPower + power);
    }

    @Override
    public void buildHealth(int health) {
        mage.setHealth(100 + health);
    }

    public void buildElement( MagicElement element) {
        mage.setElement(element);
    }

    @Override
    public AbstractCharacter getCharacter() {
        return mage;
    }
    @Override
    public void build(String name, Position pos) {
         mage = new Mage(name,pos,100);
    }

}
