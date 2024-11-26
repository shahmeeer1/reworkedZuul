/**
 * Enumeration class Item - Enumeration of items in the game.
 *
 * This class defines the items available in the game, and their attributes.
 * Items have several applications in the game, however are mainly used to
 * unlock rooms. To win the game, the player must possess three
 * items (Crown, Sword and Dragon Egg) and enter the Iron Throne room.
 *
 * All items have a weight value. The inventory has a maximum accumulated
 * weight value hence this is used to calculate whether the player carry the
 * item.
 *
 *
 * @author Shahmeer Khalid
 * @version 2024.11.26
 */
public enum Item
{
    // Define the Items in the game and their fields [name, weight, locked, tradeFor]
    SWORD("Sword", 3, false, null),
    CROWN("Crown", 2, false, null),
    DRAGONEGG("DragonEgg", 5, false, "Gold"),
    DAGGER("Dagger", 4, false, null),
    GEM("Gem", 7, false, null),
    COMPASS("Compass", 3, false, null),
    GOLD("Gold", 6, false, "Compass"),
    FEATHER("Feather", 1, true, null),
    MAGICBOOK("MagicBook", 4, true, null),
    POTION("Potion", 2, true, null);

    private String name;    // The name of the item as a string inputted by the user
    private int weight;     // The weight of the item
    private boolean locked; // Locked items may not be picked up.
    private String tradeFor;    // The item the player must possess to obtain this item through a trade

    /**
     * Constructor initialises items and fields
     */
    Item(String name, int weight, boolean locked, String tradeFor) {
        this.name = name;
        this.weight = weight;
        this.locked = locked;
        this.tradeFor = tradeFor;
    }

    /**
     * return the name of the item
     * @return String - name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the weight of the item
     * @return int - weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Return the name of the item the player must possess to
     * obtain this item through a trade.
     * @return String - name of item player must possess
     */
    public String getTradeFor(){
        return tradeFor;
    }

    /**
     * return value of 'locked' attribute
     * @return true if item is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Static method to find an item by its name.
     * @param name The name of the item to search for.
     * @return Item if found, otherwise null.
     */
    public static Item findByName(String name) {
        for (Item item : Item.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null; // Item not found.
    }
}
