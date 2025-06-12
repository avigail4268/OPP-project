package game.gameSaver;

import java.util.Stack;

public class GameCaretaker {
    private static GameCaretaker instance;
    private Stack<GameMemento> mementos;

    private GameCaretaker() {
        mementos = new Stack<>();
    }

    public static synchronized GameCaretaker getInstance() {
        if (instance == null) {
            instance = new GameCaretaker();
        }
        return instance;
    }

    public void addMemento(GameMemento m) {
        mementos.push(m);
    }

    public GameMemento getMemento() {
        if (mementos.isEmpty()) return null;
        return mementos.pop();
    }

    public boolean hasSavedGames() {
        return !mementos.isEmpty();
    }
}
