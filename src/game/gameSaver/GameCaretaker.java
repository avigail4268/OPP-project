package game.gameSaver;
import game.engine.GameWorld;
import java.util.Stack;

/**
 * GameCaretaker is a singleton class responsible for managing the game state
 * by saving and loading game mementos. It uses the Memento design pattern to
 * allow the game to revert to previous states.
 */
public class GameCaretaker {

    /**
     * Returns the singleton instance of GameCaretaker.
     * If the instance does not exist, it creates a new one.
     * @return the singleton instance of GameCaretaker
     */
    public static synchronized GameCaretaker getInstance() {
        if (instance == null) {
            instance = new GameCaretaker();
        }
        return instance;
    }

    /**
     * Saves the current game state by creating a memento from the GameWorld.
     * This memento is then added to the stack of saved states.
     * @param game the current GameWorld to be saved
     */
    public void save (GameWorld game) {
        GameOriginator originator = new GameOriginator();
        originator.setEnemies(game.getEnemies());
        originator.setGameMap(game.getMap());
        originator.setItems(game.getItems());
        originator.setPlayer();

        GameMemento memento = originator.createMemento();
        addMemento(memento);
    }

    /**
     * Loads the most recent game state from the stack of saved states.
     * If there are no saved states, it returns null.
     * @return the most recent GameMemento or null if no mementos exist
     */
    public GameMemento load() {
        if (!mementos.isEmpty()) {
            return mementos.pop();
        }
        return null;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private GameCaretaker() {
        mementos = new Stack<>();
    }

    /**
     * Adds a memento to the stack of saved game states.
     * @param m the GameMemento to be added
     */
    private void addMemento(GameMemento m) {
        mementos.push(m);
    }

    // --- Fields ---
    private static GameCaretaker instance;
    private Stack<GameMemento> mementos;
}
