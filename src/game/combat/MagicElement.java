package game.combat;

import java.util.Random;

/**
 * Represents the elemental types of magic used in combat.
 * Each element has a specific strength relationship with others,
 * determining how effective one element is against another.
 */
public enum MagicElement {
        FIRE,
        ICE,
        LIGHTNING,
        ACID;

    /**
     * Determines if this magic element is stronger than another.
     * @param other the magic element to compare against
     * @return true if this element is stronger than the other, false otherwise
     */
    public boolean isElementStrongerThan(MagicElement other) {
        switch (this) {
            case FIRE:
                return other == ICE;
            case ICE:
                return other == LIGHTNING;
            case LIGHTNING:
                return other == ACID;
            case ACID:
                return other == FIRE;
            default:
                return false;
        }
    }

    /**
     * Returns the magic element associated with this attacker.
     * This method is used to determine elemental affinities and weaknesses during combat.
     * @return the MagicElement of this attacker
     */
    public static MagicElement getElement() {
        Random r = new Random();
        int type = r.nextInt(3);
        if (type == 0) {
            return FIRE;
        }
        else if (type == 1) {
            return ICE;
        }
        else if (type == 2) {
            return LIGHTNING;
        }
        else {
            return ACID;
        }
    }
}

