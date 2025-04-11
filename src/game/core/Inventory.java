package game.core;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<GameItem> items;

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

    public int getItemCount() {
        return items.size();
    }

    public boolean isHealthPotion(int i){
        GameItem item = items.get(i);

        if (!this.isPowerPotion(i) && item instanceof Potion)
            return true;
        return false;
    }

    public boolean isPowerPotion(int index) {
        GameItem item = items.get(index);
        return item instanceof PowerPotion;
    }

    public boolean useItem(int index) {
        // TODO check if useItem is needed
    return false;
    }
    public boolean equals (Object obj) {
        if ( obj instanceof Inventory)
        {
            Inventory other = (Inventory) obj;
            return this.items.equals(other.items);
        }
        return false;
    }
    public String toString() {
        return "Inventory";
    }
}
