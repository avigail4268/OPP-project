package game.characters;
import game.core.Inventory;
import game.items.GameItem;
import game.items.Interactable;
import game.items.Potion;
import game.items.PowerPotion;
import game.map.Position;

import java.util.List;

public abstract class PlayerCharacter extends AbstractCharacter {

    private String name;
    private Inventory inventory;
    private int treasurePoints;


    public PlayerCharacter(String playerName, Position position,int health) {
        super(position, health);
        this.name = playerName;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

     public String getName() {
        return name;
    }

    public boolean addToInventory(GameItem item) {
        if (item == null) {
            return false;
        }
        return inventory.addItem(item);
    }

    public boolean usePotion() {
        // if has health potion in inventory use it
        List<GameItem> item = inventory.getItems();
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i) instanceof Potion potion) {
                potion.interact(this);
                return true;
            }
        }
        return false;
    }

    public boolean usePowerPotion() {
        // if has power potion in inventory use it
        List<GameItem> item = inventory.getItems();
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i) instanceof PowerPotion powerPotion) {
                powerPotion.interact(this);
                return true;
            }
        }
         return false;
    }

    public void updateTreasurePoint(int amount){
        this.treasurePoints += amount;
    }

    public int getTreasurePoints(){
         return treasurePoints;
    }
}
