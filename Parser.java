import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a three word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling, David J. Barnes and Shahmeer Khalid
 * @version 2024.11.23
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * Reads and returns a command object consisting of
     * up to the first three words of the user input
     * @return The next command from the user.
     */
    public Command getCommand() 
    {
        String inputLine;   // will hold the full input line
        String word1 = null, word2 = null, word3 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to three words on the line.
        Scanner tokenizer = new Scanner(inputLine);

        // try - catch block to handle exceptions
        try{
            word1 = tokenizer.next();
            word2 = tokenizer.next();
            word3 = tokenizer.next();
        }catch(NoSuchElementException e){
            // exception created if command does not contain enough words
            // This will allow us to take the user's
            // no action needed to handle exception.
            ;
        }


        // Now check whether this command word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(word1)) {
            return new Command(word1, word2, word3);
        }
        else {
            return new Command(null, word2, word3);
        }
    }

    /**
     * Print out a list of valid command words.
     * Also prints out a description of how to use them.
     * Source to table format:
     * https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/Java-print-table-format-printf-chart-console-scanner-println-line
     */
    public void showCommands()
    {
        commands.showAll();
        System.out.println();
        // TABLE
        System.out.println("+---------------------------+-------------------------------------------------+");
        System.out.printf("| %-25s | %-47s |\n", "Command", "Description");
        System.out.println("+---------------------------+-------------------------------------------------+");

        // Commands and descriptions
        System.out.printf("| %-25s | %-47s |\n", "go [Direction]", "Move to a new location.");
        System.out.printf("| %-25s | %-47s |\n", "quit", "Quit the game.");
        System.out.printf("| %-25s | %-47s |\n", "help", "Display this tutorial.");
        System.out.printf("| %-25s | %-47s |\n", "search inventory", "Display the contents of your inventory.");
        System.out.printf("| %-25s | %-47s |\n", "search room", "Search your current location for items.");
        System.out.printf("| %-25s | %-47s |\n", "take [item]", "Take an item.");
        System.out.printf("| %-25s | %-47s |\n", "back", "Go back to the previous location.");
        System.out.printf("| %-25s | %-47s |\n", "drop [item]", "Drop an item from your inventory.");
        System.out.printf("| %-25s | %-47s |\n", "trade [npc name] offer", "View an NPC's trade offer");
        System.out.printf("| %-25s | %-47s |\n", "trade [npc name] [item]", "Initiate a trade with an NPC");

        System.out.println("+---------------------------+-------------------------------------------------+");
        System.out.println("Use these commands to navigate through the game");
    }
}
