package game.characterBuilders;
import game.characters.AbstractCharacter;
import game.map.Position;

public interface CharacterBuilder {
    void build(String type,Position pos);
    void buildPower(int power);
    void buildHealth(int health);
    AbstractCharacter getCharacter();
}
