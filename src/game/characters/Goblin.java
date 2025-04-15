package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter {

    private int agility;

    public Goblin(Position pos, int health) {
        super(pos,health);
        agility = new Random().nextInt(80);
    }

    @Override
    public String getDisplaySymbol() {
        return DisplaySymbols.Goblin;
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
