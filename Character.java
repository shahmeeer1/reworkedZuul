/**
 * Class Character - Class represents a player in the game.
 * Serves as a base class for NPc and Player, and provides common inventory attribute
 * and methods.
 * Subclasses can extend class to create specific charcters such as NPCs or the player
 * of the game.
 *
 * @author Shahmeer Khalid
 */


import java.util.ArrayList;

public class Character{
    protected ArrayList<Item> items;

    /**
     * Constructor instantiates instances
     */
    public Character(){
        this.items = new ArrayList<>();
    }

    /**
     * Method to add an item to inventory
     * @param item - Item to add to inventory
     */
    public void addItem(Item item){
        items.add(item);
    }

    /**
     * Method to remove an item from inventory
     * @param item - Item to remove from inventory
     */
    public void removeItem(Item item){
        items.remove(item);
    }

    /**
     * Returns List of items in character's inventory
      * @return ArrayList<items>
     */
    public ArrayList<Item> getInventory(){
        return items;
    }

    /**
     * Checks if character inventory contains an item
     * @param item - item to check for
     * @return true if inventoru contains item
     */
    public boolean hasItem(Item item){
        return items.contains(item);
    }

    /**
     * Output the contents of the inventory to the terminal
     */
    public void printInventory(){
        // Override
    }
}