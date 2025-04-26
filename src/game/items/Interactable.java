package game.items;

import game.characters.PlayerCharacter;

/**
 * Represents an object that can interact with a player character.
 *
 * Interactable objects can be interacted with or collected.
 */
public interface Interactable {

    /**
     * Performs an interaction between this object and the player character.
     *
     * @param c the player character interacting with this object
     */
    void interact(PlayerCharacter c);

    /**
     * Collects this object into the player character's inventory or equipment.
     *
     * @param c the player character collecting this object
     */
    void collect(PlayerCharacter c);
}
