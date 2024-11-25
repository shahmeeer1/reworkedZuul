/**
 * Class RoomManager - A class that keeps track of all rooms and room status.
 *
 * This class allows for additional features to be added to the game using rooms
 * In this case, it has been used to implement a transporter room which teleports
 * the player to a random room in the game.
 *
 * The class only makes use of static fields and methods and no instance is created.
 *
 * @author Shahmeer Khalid
 * @version 2024.11.23
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class RoomManager {

    private  HashMap<String, Room> roomList = new HashMap();
    private  Room transporterRoom;    // Transporter room teleports player to a random unlocked room

    /**
     * Adds room to list of rooms
     * @param room
     */
    public Room addRoom(String name, Room room) {
        roomList.put(name, room);
        return room;
    }

    public  Room getRoom(String name){
        return roomList.get(name);
    }



    /**
     * Gives room transporter room status
     * @param room - the room to set as transporter room
     */
    public void setTransporterRoom(Room room){transporterRoom = room;}

    /**
     * Returns a randomly selected room from the list of rooms
     * @return Room
     */
    public  Room getRandomRoom() {
        Random random = new Random();
        Set<String> keySet = roomList.keySet();
        ArrayList<String> keys = new ArrayList<>(keySet);
        Room newRoom;
        do {
            // Get random room from roomList and assign to newRoom
            newRoom = roomList.get(keys.get( random.nextInt(keys.size()) ) );
        }// Do until you get a room that isn't transporter room
        while (newRoom.equals(transporterRoom));

        return newRoom;

    }

    /**
     * Checks if current room has transporter room status
     * @param currentRoom
     * @return boolean true if room has transporter room status
     */
    public boolean checkTransporterRoom(Room currentRoom){
        if (transporterRoom.equals(currentRoom)){
            return true;
        }
        else{
            return false;
        }
    }


}