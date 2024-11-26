/**
 * Class Npc - Non-playable characters can be created and added to the game.
 *
 * NPCs can be traded with to obtain items required to advance in the game
 * Apart from trading with the user, the NPC can move between connecting rooms however will never
 * enter locked or transporter rooms.
 *
 * @author Shahmeer Khalid
 * @version 2024.11.23
 */


import java.util.ArrayList;
import java.util.Random;

public class Npc extends Character{

    private String name;        // name of NPC
    private String description; // Description of NPC
    private Room currentRoom;   // The room the NPc is currently in
    private int counter = 0;    // The number of movements the player has made since the last NPC move
    private int movementFrequency = 2;  // How many player moves the NPc must wait before moving
    private static ArrayList<Npc> npcList = new ArrayList<>();  // Stores all instances of NPC Class


    public Npc(String name, String description, Room startingRoom){
        super();
        this.name = name;
        this.description = description;
        this.currentRoom = startingRoom;
        npcList.add(this);  // Add NPC to list of npc instances
    }

    /**
     * Returns class field which contains list of all instances
     * @return
     */
    public static ArrayList<Npc> getNpcList() {
        return npcList;
    }

    /**
     * Class method to move every npc to a new room. Npcs are only moved
     * to rooms that are unlocked and are connected to their current room.
     */
    public static void moveNpc(){
        Random randnum = new Random();
        Room newRoom;

        // Every npc is checked
        for (Npc npc: npcList){
            // Npc may only move on every n-th turn. n defaults to 2
            if (npc.getCounter() < npc.getMovementFrequency()){
                // increment counter by 1
                npc.updateCounter(false);
                continue;
            }
            else{npc.updateCounter(true);}  // Indicate that the npc will move on  the next turn

            Room currentRoom = npc.getCurrentRoom();    // Retrieve room npc is currently in
            ArrayList<Room> exits = currentRoom.getExits(); // Get list of all rooms connected to current room

            do{
                // Randomly select a room from list of exits
                newRoom = exits.get(randnum.nextInt(exits.size()));

            } while(newRoom.isLocked()); // Keep selecting room until one is selected which is not locked

            // move npc to new room
            npc.setCurrentRoom(newRoom);
            System.out.println(npc.getName() + " has moved to a new city...");

        }
    }

    /**
     * Retrieve an npc by name
     * @param name - name of npc to retrieve
     * @return - npc object
     */
    public static Npc getNpc(String name){
        // Iterate through list of npcs
        for(Npc npc: npcList){
            // Check every npcs name to see if there is a match
            if (npc.getName().equalsIgnoreCase(name)){
                return npc;
            }
        }
        return null;    // No match was found
    }

    /**
     * Check if a npc is in the same room as the player
     * If so, let the player know by outputting a message.
     * @param currentRoom - room that the player is currently in
     */
    public static void npcInRoom(Room currentRoom){
        // Check every npc
        for (Npc npc: npcList){
            try{
                if (npc.getCurrentRoom().equals(currentRoom)) { // If npcs and player in same room
                    System.out.println();
                    System.out.println("You've bumped into " + npc.getName());
                }
            } catch (Exception e) {System.out.println("Npc not initialised properly");} // If npc is not in a room
        }
    }


    /**
     * Displayt he contents of the npcs inventory to the player
     */
    public void printInventory(){
        String inventory = name + "'s Inventory:";
        for (Item item : items){
            // Retrieve the name of every object and append to string
            inventory += " " + item.getName();
        }
        System.out.println(inventory);
    }


    /**
     * Set the room the NPC is in.
     * @param room - room to move NPc to.
     */
    public void setCurrentRoom(Room room){
        currentRoom = room;
    }

    /**
     * Gets the room the NPc is currently in.
     * @return current Room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Get NPCs description
     * @return - npc description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the NPCs name
     * @return - name of npc
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current value of the NPCs movement counter
     * @return - integer value of counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * The counter counts the delay in npc movement
     * @param reset resets the counter to 0 if true
     */
    public void updateCounter(boolean reset) {
        if (reset) {    // If true
            counter = 0;  // Reset the counter
        } else {
            counter++;  // Increment the counter by 1
        }
    }

    /**
     * Get the number of player moves the NPC must wait before moving
     * @return integer value for movement frequency
     */
    public int getMovementFrequency() {
        return movementFrequency;
    }

    /**
     * Set the number of player moves the NPC must wait before moving
     * @param movementFrequency - how often the npc can move
     */
    public void setMovementFrequency(int movementFrequency) {
        this.movementFrequency = movementFrequency;
    }

}
