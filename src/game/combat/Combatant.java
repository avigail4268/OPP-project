package game.combat;

import game.map.Position;

public interface Combatant {
    int getHealth();
    boolean setHealth(int health);
    void receiveDamage(int amount, Combatant source);
    void heal(int amount);
    boolean isDead();
    int getPower();
    boolean tryEvade();
    Position getPosition();
    MagicElement getMagicElement();
    void attack(Combatant target);
}
