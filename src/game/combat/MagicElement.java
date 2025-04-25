package game.combat;

import java.util.Random;

/**
 * MagicElement represents the different types of magic elements in the game.
 * Each element has a specific strength against other elements.
 */
public enum MagicElement {

    FIRE,
    ICE,
    LIGHTNING,
    ACID;

    /**
     * Determines whether this element is stronger than another.
     *
     * @param other the element to compare against
     * @return true if this element is stronger than other, false otherwise
     */
    public boolean isElementStrongerThan(MagicElement other) {
        return switch (this) {
            case FIRE -> other == ICE;
            case ICE -> other == LIGHTNING;
            case LIGHTNING -> other == ACID;
            case ACID -> other == FIRE;
        };
    }

    /**
     * Randomly selects a MagicElement, with a bias toward the first three elements
     * (FIRE, ICE, LIGHTNING). ACID has a very low chance due to the bounds of nextInt(4).
     *
     * @return a randomly chosen MagicElement
     */
    public static MagicElement getElement() {
        Random r = new Random();
        int type = r.nextInt(4);
        if (type == 0) {
            return FIRE;
        } else if (type == 1) {
            return ICE;
        } else if (type == 2) {
            return LIGHTNING;
        } else {
            return ACID;
        }
    }
}
