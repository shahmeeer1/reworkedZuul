/**
 * Class Character - Class represents a player in the game.
 * Serves as a base class for NPc and Player, and provides common inventory attribute
 * and methods.
 * Subclasses can extend class to create specific chaarcters such as NPCs or the player
 * of the game.
 *
 * @author Shahmeer Khalid
 */


import java.util.ArrayList;

public class Character{
    protected ArrayList<Item> items;


    public Character(){
        this.items = new ArrayList<>();
    }

    /**
     * Method to add an item to inventory
     * @param item
     */
    public void addItem(Item item){
        items.add(item);
    }

    /**
     * Method to remove an item from inventory
     * @param item
     */
    public void removeItem(Item item){
        items.remove(item);
    }

    /**
     * Output the contents of the inventory to the terminal
     */
    public void printInventory(){
        // Override
    }
}