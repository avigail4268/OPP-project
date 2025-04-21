package game.combat;

import java.util.Random;

public enum MagicElement {
        FIRE,
        ICE,
        LIGHTNING,
        ACID;

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

