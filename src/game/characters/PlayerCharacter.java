package game.characters;
import game.core.Inventory;
import game.items.GameItem;
import game.items.Interactable;
import game.items.Potion;
import game.map.Position;

public abstract class PlayerCharacter extends AbstractCharacter {

    private String name;
    private Inventory inventory;
    private int treasurePoints = 0;

    public PlayerCharacter(String playerName, Position position) {
        super(position);
        this.name = playerName;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

     public String getName() {
        return name;
    }

    public boolean addToInventory(GameItem item) {
        return inventory.addItem(item);
    }

    public boolean usePotion() {
         // if has health potion in inventory use it
        for (int i = 0; i < inventory.getItemCount(); i++) {
            if (inventory.isHealthPotion(i)) {
                // use the potion
                Interactable potion = (Interactable) inventory.getItems().get(i);
                potion.interact(this);
                return true;
            }
        }
        return false;
    }

    public boolean usePowerPotion() {
        // if has power potion in inventory use it
         for (int i = 0; i < inventory.getItemCount(); i++) {
             if (inventory.isPowerPotion(i)) {
                 return true;
             }
         }
         return false;
    }

    public boolean updateTreasurePoint(int amount){
         this.treasurePoints = treasurePoints + amount;
         //check if updated successfully
        return true;
    }

    public int getTreasurePoints(){
         return treasurePoints;
    }
}
