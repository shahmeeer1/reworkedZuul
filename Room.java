import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String description;
    private String quest;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> storage;

    // locked rooms cannot be accessed but may be unlocked using certain items
    private boolean locked;
    private List<Item> requiredItems = new ArrayList<>();



    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open courtyard".
     * @param description The room's description.
     */
    public Room(String name, String description, boolean locked, Item...unlockItems)
    {
        this.description = description;
        exits = new HashMap<>();
        this.locked = locked;
        storage = new ArrayList<>();


        // If the room is locked, add the items required to  unlock it to the list.
        if (locked){
            for (Item item: unlockItems){
                requiredItems.add(item);
            }
        }
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }



    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    /**
     * Return an arraylist containing the exit rooms connected to the current room
     * @return
     */
    public ArrayList<Room> getExits(){
        // exits.values() returns a collection which is converted to an arraylist.
        return new ArrayList<>(exits.values());
    }

    /**
     * Returns list of items required to unlock room if room is locked
     * If room is unlocked, returns null
     * @return List if room is locked
     */
    public List<Item> getRequiredItems(){
        return requiredItems;   // Return list of items to unlock room
    }

    /**
     * Method to retrieve Araylist of room storage
     * @return
     */
    public ArrayList<Item> getStorage(){
        return storage;
    }

    /**
     * Display the contents of the room's storage as a string
     */
    public void showStorage(){
        String stored = "Storage:";
        for (Item item: storage){
            stored += " " + item.getName();
        }
        System.out.println(stored);
    }

    /**
     * Method to add items to room storage
     * @param item
     */
    public void addToStorage(Item item){
        storage.add(item);
    }

    public Item getItem(String itemName){
        for (Item item: storage){
            if (!(itemName == null) && item.getName().equalsIgnoreCase(itemName)){
                return item;
            }
        }
        return null;
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlockRoom(){
        locked = false;
    }

    public Item removeItem(String itemName){
        for (Item item: storage) {
            if (!(itemName == null) && item.getName().equalsIgnoreCase(itemName)) {
                storage.remove(item);
                return item;
            }
        }
        return null;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    /**
     *  Method to set the quest for a room. A quest adds to the game's storyline and give the player information
     *  on where they can find the items to unlock a locked room
     */
    public String getQuest(){return quest;}
}

