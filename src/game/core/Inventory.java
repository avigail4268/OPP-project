package game.core;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public Inventory() {
        items = new ArrayList<>();
    }

    public boolean addItem(GameItem item) {
        if (item != null){
            items.add(item);
            return true;
        }
        return false;
    }

    public boolean removeItem(GameItem item) {
        if (items.contains(item)){
            items.remove(item);
            return true;
        }
        return false;
    }

    public List<GameItem> getItems() {
        return items;
    }
    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Inventory other)
        {
            return this.items.equals(other.items);
        }
        return false;
    }
    @Override
    public String toString() {
        return "Inventory";
    }

    private List<GameItem> items;
}
