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
        //add item to inventory
        items.add(item);

        // TODO check if item was added successfully and return the check result
        return true;
    }

    public boolean removeItem(GameItem item) {
        items.remove(item);

        // TODO check if item was removed successfully and return the check result
        return true;
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
    return false;
    }

}
