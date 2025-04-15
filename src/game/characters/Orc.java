package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

public class Orc extends Enemy implements MeleeFighter, PhysicalAttacker {

    private double resistance;

    public Orc(Position pos, int health) {
        super(pos,health);
        resistance = new Random().nextDouble(0.5);
    }

    @Override
    public String getDisplaySymbol() {
        return "";
    }

    @Override
    public MagicElement getMagicElement() {
        return null;
    }

    @Override
    public void fightClose(Combatant target) {

    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return false;
    }

    @Override
    public void attack(Combatant target) {

    }

    @Override
    public boolean isCriticalHit() {
        return false;
    }
}
