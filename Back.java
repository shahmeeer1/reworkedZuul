/**
 * Class Back - A stack used to implement a back feature into the game.
 *
 * This class allows the user to step back to the room they entered the current room from.
 * This class is essentially an implementation of a stack with a slight modification to
 * the pop method; to prevent popping off the element at index 0.
 *
 * @author Shahmeer Khalid
 * @version 2024.11.23
 */

import java.util.ArrayList;

public class Back {
    private ArrayList<Room> stack;
    private int pointer;    // Every stack needs a pointer

    public Back(){
        stack = new ArrayList<>();
        pointer = 0;
    }

    /**
     * Push method to add rooms to the stack.
     * @param room - the room just exited
     */
    public void push(Room room){
        stack.add(pointer, room);   // Add item to stack
        pointer++;  // Increment pointer
    }

    /**
     * Method used to pop a room off the top of the stack.
     * This is the room that the player is returning to.
     * The push method has been modified to keep the bottom element
     * on the stack. This is the room where the player starts the game.
     * @return Room - previous room the player was in
     */
    public Room pop(){
        assert pointer > 0;
        if (pointer == 1){
            return null;    // Do not remove bottom element from stack
        }
        pointer -= 1;   // decrement pointer
        return stack.remove(pointer);   // remove element at pointer

    }
}
