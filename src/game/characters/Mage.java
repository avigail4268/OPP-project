package game.characters;

import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.MagicElement;
import game.combat.RangeFighter;
import game.map.Position;


public class Mage extends PlayerCharacter implements RangeFighter, MagicAttacker {

    public Mage(String name,Position pos,int health) {
        super(name,pos,health);
        this.element = MagicElement.getElement();
    }
    @Override
    public String getDisplaySymbol() {
        return "MAGE";
    }
    @Override
    public MagicElement getMagicElement() {
        return element;
    }
    @Override
    public void attack(Combatant target) {
        castSpell(target);
    }
    @Override
    public void calculateMagicDamage(Combatant target) {
        //TODO: check if this is correct
        double totalDamage = this.getPower() * 1.5;
        MagicElement targetElement = target.getMagicElement();
        if (targetElement != null) {
            if (this.element.isElementStrongerThan(targetElement)) {
                totalDamage *= 1.2;
                System.out.println("Mage attack by magic, his element is stronger than the enemy's");
            } else if (targetElement.isElementStrongerThan(this.element)) {
                totalDamage *= 0.8;
                System.out.println("Mage attack by magic, his element is weaker than the enemy's");
            }
        }
        target.receiveDamage((int) totalDamage, this);
        System.out.println(this.getName() + " attacked the enemy for: " + (int) totalDamage + " damage.");
    }
    @Override
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return element.isElementStrongerThan(other.getMagicElement());
    }
    @Override
    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(), target.getPosition())){
            castSpell(target);
        }
    }
    @Override
    public int getRange() {
        return 2;
    }
    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }
    public boolean equals (Object obj) {
        if (obj instanceof Mage) {
            Mage other = (Mage) obj;
            return this.getName().equals(other.getName()) && this.getPosition().equals(other.getPosition());
        }
        return false;
    }

    public String toString() {
        return "Mage: " + this.getName() + " at " + getPosition();
    }

    private MagicElement element;

}
