/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Back stack;
    private RoomManager roomManager;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        stack = new Back();
        parser = new Parser();
        roomManager = new RoomManager();
        createRooms();

    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room kingsLanding, winterfell, theWall, riverrun,
                dragonStone, dorne, casterlyRock, highGarden, stormsEnd, ironThrone;


        // create the rooms. STRING NAMES OF LOCATIONS ARE IN LOWER CASE WITH SPACES FORMAT.
        kingsLanding = roomManager.addRoom("kingsLanding", new Room("kings landing","in King's Landing", true, Item.GEM));
        winterfell = roomManager.addRoom("winterfell",new Room("winterfell","in winterfell", false));
        theWall = roomManager.addRoom("the wall",new Room("the wall", "at the wall", false));
        riverrun = roomManager.addRoom("riverrun",new Room("riverrun", "in riverrun", false));
        dragonStone = roomManager.addRoom("dragon stone",new Room("dragon stone", "in dragonstone", false));
        dorne = roomManager.addRoom("dorne",new Room("dorne", "in Dorne", false));
        casterlyRock = roomManager.addRoom("casterly rock",new Room("casterly rock", "in casterly Rock", false));
        highGarden = roomManager.addRoom("high garden",new Room("high garden", "at high garden", false));
        stormsEnd = roomManager.addRoom("storms end",new Room("storms end", "in storms end", true, Item.DAGGER));
        ironThrone = roomManager.addRoom("iron throne",new Room("iron throne", "sitting on the iron throne", true,
                                Item.SWORD, Item.CROWN, Item.DRAGONEGG));

        // initialise room exits
        kingsLanding.setExit("north", ironThrone);
        kingsLanding.setExit("east", dragonStone);
        kingsLanding.setExit("south", highGarden);
        kingsLanding.setExit("west", casterlyRock);

        ironThrone.setExit("south", kingsLanding);

        casterlyRock.setExit("east", kingsLanding);
        casterlyRock.setExit("north", riverrun);

        winterfell.setExit("north", theWall);
        winterfell.setExit("south", dorne);
        winterfell.setExit("west", riverrun);

        theWall.setExit("south", winterfell);

        riverrun.setExit("east", winterfell);
        riverrun.setExit("south", casterlyRock);

        dragonStone.setExit("south", stormsEnd);
        dragonStone.setExit("west", kingsLanding);

        dorne.setExit("north", winterfell);
        dorne.setExit("east", stormsEnd);
        dorne.setExit("west", highGarden);

        casterlyRock.setExit("north", riverrun);
        casterlyRock.setExit("east", kingsLanding);

        highGarden.setExit("north", kingsLanding);
        highGarden.setExit("east", dorne);

        stormsEnd.setExit("north", dragonStone);
        stormsEnd.setExit("west", dorne);

        addItems(); // Add items to room storage
        currentRoom = winterfell;  // Start game in winterfell
        stack.push(currentRoom);    // Add  first location to stack
        roomManager.setTransporterRoom(theWall); // Set transporter room
    }

    private void addItems(){
        roomManager.getRoom("high garden").addToStorage(Item.COMPASS);
        roomManager.getRoom("storms end").addToStorage(Item.CROWN);
        roomManager.getRoom("storms end").addToStorage(Item.CROWN);
        roomManager.getRoom("casterly rock").addToStorage(Item.DAGGER);
        roomManager.getRoom("casterly rock").addToStorage(Item.POTION);
        roomManager.getRoom("riverrun").addToStorage(Item.GEM);
        roomManager.getRoom("riverrun").addToStorage(Item.MAGICBOOK);
        roomManager.getRoom("dragonstone").addToStorage(Item.SWORD);
        roomManager.getRoom("dragonstone").addToStorage(Item.FEATHER);
    }

    private void createNpc(){
        // Create two NPCs. Add their names, descriptions and starting locations
        Npc jon = new Npc("Jon", "Jon Snow, the son of Ned Stark", roomManager.getRoom("winterfell"));
        Npc merchant = new Npc("Merchant", "The merchant is a trader from Braavos. A mysterious island accross the Narrow Sea", roomManager.getRoom("riverrun"));
        jon.addItem(Item.GOLD);
        merchant.addItem(Item.DRAGONEGG);

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
