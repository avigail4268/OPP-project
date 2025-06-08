package game.characterBuilders;

import game.characters.*;
import game.map.Position;

public class PlayerBuilder implements CharacterBuilder {
    private String name;
    private int health = 100;
    private Position position;

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void randomizeStats() {
        // לא חובה אם הכל מגיע מה-GUI
    }

    @Override
    public void setPosition(Position pos) {
        this.position = pos;
    }

    @Override
    public void setPower(int power) {//TODO
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public AbstractCharacter build(String type) {
        return switch (type) {
            case "Archer" -> new Archer(name, position, health);
            case "Mage" -> new Mage(name, position, health);
            case "Warrior" -> new Warrior(name, position, health);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}