/**
 * Class Player - The Player class extends its parent class Character, and defines
 * the attributes and methods specific to the player of the game. This includes
 * methods relating to the inventory and adds weight functionality to limit the
 * number of items the  player may carry.
 *
 * @author Shahmeer Khalid
 * @version 2024.11.26
 */


public class Player extends Character{
    private int maxWeight = 10;
    private int weight;

    public Player(){
        super();    // Call constructor of parent class
    }

    /**
     * Method to add an item to inventory if the player has space.
     * @param item
     */
    public void addItem(Item item){
        //  Add the item to inventory
        assert item != null;
        items.add(item);
        weight += item.getWeight(); // add item weight
        System.out.println(item.getName() + " added to inventory.");
    }

    /**
     * Check if player's inventory is empty
     * @return true if players inventory is empty
     */
    public boolean invIsEmpty(){
        return items.isEmpty();
    }

    /**
     * Calculate the amount of free space the player has in their inventory
     * @return value form remaining space
     */
    public int availableSpace(){
        return (maxWeight - weight);
    }

    /**
     * Method to remove an item from inventory
     * @param item
     */
    public void removeItem(Item item){
        items.remove(item); // Remove item from inventory
        weight -= item.getWeight(); // Remove item weight from accumulated weight value
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