package game.characterBuilders;
import game.characters.AbstractCharacter;
import game.map.Position;

public interface CharacterBuilder {
    AbstractCharacter build(String type,Position pos);
//    void buildPosition(Position pos);
    void buildPower(int power);
    void buildHealth(int health);
    void randomizeStats();
}
