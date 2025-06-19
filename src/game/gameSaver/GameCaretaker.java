package game.gameSaver;

import game.engine.GameWorld;

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
        System.out.println("Added memento");
    }

    public GameMemento getMemento() {
        if (mementos.isEmpty()){
            return null;
        }
        return mementos.pop();
    }

    public void save (GameWorld game) {
        GameOriginator originator = new GameOriginator();
        originator.setEnemies(game.getEnemies());
        originator.setGameMap(game.getMap());
        originator.setItems(game.getItems());
        originator.setPlayer(game.getPlayer());

        GameMemento memento = originator.createMemento();
        addMemento(memento);
    }


    public GameMemento load() {
        if (!mementos.isEmpty()) {
            return mementos.pop();
        }
        return null;
    }
}
