package game.characters;

import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker {
    private int defence;
    public Warrior(String playerName, Position position) {
        super(playerName, position);
        Random random = new Random();
        this.defence = random.nextInt(120);
    }

    @Override
    public String getDisplaySymbol() {
        return "W";
    }

    @Override
    public boolean setVisible(boolean visible) {
        return false;
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

    public void receiveDamage(int amount, Combatant source) {
        //subtract amount from health
        if (tryEvade()) return;
    }
}
