
/**
 * Enumeration class Item - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum Item
{
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

    private String name;
    private int weight;
    private boolean locked;
    private String tradeFor;

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
    
    public String tradeFor(){
        return tradeFor;
    }
}
