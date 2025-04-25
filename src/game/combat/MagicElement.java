package game.combat;

import java.util.Random;

public enum MagicElement {
        FIRE,
        ICE,
        LIGHTNING,
        ACID;

    public boolean isElementStrongerThan(MagicElement other) {
        // Check if the current element is stronger than the other element
        return switch (this) {
            case FIRE -> other == ICE;
            case ICE -> other == LIGHTNING;
            case LIGHTNING -> other == ACID;
            case ACID -> other == FIRE;
        };
    }
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

