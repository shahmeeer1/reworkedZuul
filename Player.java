/**
 * Class Player - The class PPlayer extends the parent class Character and defines
 * the attributes and methods specific to the player of the game. This includes
 * methods relating to the inventory and adds weight functionality to limit the
 * number of items the  player may carry.
 *
 * @author Shahmeer Khalid
 */

import java.util.ArrayList;

public class Player extends Character{
    private int maxWeight = 10;
    private int weight;

    public Player(){
        super();
    }

    /**
     * Method to add an item to inventory if the player has space.
     * @param item
     */
    public void addItem(Item item){
        // Check if additional weight of item is <= maximum weight capacity
        if ((item.getWeight() + weight) <= maxWeight){
            items.add(item);    // If so then add the item to inventory
            System.out.println(item.getName() + " added to inventory.");
        }
        else{   // If not then reject the item
            System.out.println("Not enough space in inventory");
        }
    }

    /**
     * Method to remove an item from inventory
     * @param item
     */
    public void removeItem(Item item){
        items.remove(item);
        System.out.println(item.getName() + " removed from inventory.");
    }

    /**
     * Method to display the contents of inventory terminal
     */
    public void printInventory(){
        if (items.isEmpty()){
            // If inventory is empty display message
            System.out.println("Inventory is empty.");
        }
        else{
            // Instantiate string
            String inventory = "Inventory:";
            for (Item item : items){
                // Add name of each item to string
                inventory += " " + item.getName();
            }
            // Output string
            System.out.println(inventory);
        }
    }
}