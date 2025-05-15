package game.observer;

/**
 * Interface for observers that need to be notified when the game state updates.
 */
public interface GameObserver {

    /**
     * Called when the game state is updated.
     */
    void onGameUpdated();
}

