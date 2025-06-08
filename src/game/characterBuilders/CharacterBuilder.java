package game.characterBuilders;

import game.characters.AbstractCharacter;
import game.map.Position;

public interface CharacterBuilder {
    AbstractCharacter build();
    void setPosition(Position pos);
    void setPower(int power);
    void randomizeStats();
}
