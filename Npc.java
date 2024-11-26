
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

    public static void moveNpc(){
        Random randnum = new Random();
        Room newRoom;

        for (Npc npc: npcList){
            // Npc may only move on every n-th turn. n defaults to 2
            if (npc.getCounter() < npc.getMovementFrequency()){
                // increment counter by 1
                npc.updateCounter(false);
                continue;
            }
            else{npc.updateCounter(true);}

            Room currentRoom = npc.getCurrentRoom();
            ArrayList<Room> exits = currentRoom.getExits();

            do{
                // Randomly select a room from list of exits
                newRoom = exits.get(randnum.nextInt(exits.size()));

            } while(newRoom.isLocked()); // Keep selecting room until one is selected which is not locked

            // move npc to new room
            npc.setCurrentRoom(newRoom);
            System.out.println(npc.getName() + " has moved to a new city...");

        }
    }

    public static Npc getNpc(String name){
        for(Npc npc: npcList){
            if (npc.getName().equalsIgnoreCase(name)){
                return npc;
            }
        }
        return null;
    }

    public static void npcInRoom(Room currentRoom){
        for (Npc npc: npcList){
            try{
                if (npc.getCurrentRoom().equals(currentRoom)) {
                    System.out.println();
                    System.out.println("You've bumped into " + npc.getName());
                }
            } catch (Exception e) {System.out.println("Npc not initialised properly");}
        }
    }


    public void printInventory(){

        String inventory = name + "'s Inventory:";
        for (Item item : items){
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
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the NPCs name
     * @return
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
     * @param movementFrequency - int
     */
    public void setMovementFrequency(int movementFrequency) {
        this.movementFrequency = movementFrequency;
    }

}
