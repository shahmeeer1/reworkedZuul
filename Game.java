import java.util.List;


/**
 *  This class is the main class of the "Westeros" application.
 *  "Westeros" is a text based adventure game, inspired by the
 *  book series 'A song of Ice and Fire' and television series
 *  'Game of Thrones' written by George R.R Martin and produced by HBO.
 *
 *  Names of some characters, locations and the name of the game 'Westeros'
 *  are inspired by 'A song of Ice and Fire'. The storyline of the game however,
 *  is my own work.
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, NPCs, items; creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling, David J. Barnes and Shahmeer Khalid
 * @version 2024.11.23
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Back stack;
    private RoomManager roomManager;
    private Player player;
    private boolean active = true;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        stack = new Back();
        parser = new Parser();
        roomManager = new RoomManager();
        player = new Player();
        createRooms();
        createNpc();

    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room kingsLanding, winterfell, theWall, riverrun,
                dragonStone, dorne, casterlyRock, highGarden, stormsEnd, ironThrone;


        // create the rooms. STRING NAMES OF LOCATIONS ARE IN LOWER CASE WITH SPACES FORMAT.
        kingsLanding = roomManager.addRoom("kings landing", new Room("kings landing","in King's Landing", true, Item.GEM));
        winterfell = roomManager.addRoom("winterfell",new Room("winterfell","in winterfell", false));
        theWall = roomManager.addRoom("the wall",new Room("the wall", "at the wall", false));
        riverrun = roomManager.addRoom("riverrun",new Room("riverrun", "in riverrun", false));
        dragonStone = roomManager.addRoom("dragon stone",new Room("dragon stone", "in dragonstone", true, Item.DAGGER));
        dorne = roomManager.addRoom("dorne",new Room("dorne", "in Dorne", false));
        casterlyRock = roomManager.addRoom("casterly rock",new Room("casterly rock", "in casterly Rock", false));
        highGarden = roomManager.addRoom("high garden",new Room("high garden", "at high garden", false));
        stormsEnd = roomManager.addRoom("storms end",new Room("storms end", "in storms end", false));
        ironThrone = roomManager.addRoom("iron throne",new Room("iron throne", "sitting on the iron throne", true,
                                Item.SWORD, Item.CROWN, Item.DRAGONEGG));

        // Add quests for locked rooms. A quest is simply some text to advance the story of the game
        //  and assist the player
        ironThrone.setQuest("Do you dare to play the Game of Thrones...");
        dragonStone.setQuest("The key to Dragon Stone lies in the lion's den");
        kingsLanding.setQuest("King's Landing...The heart of the Seven Kingdoms.\nTo gain entry, seek the item hidden where the river bends...");

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
        roomManager.getRoom("dragon stone").addToStorage(Item.CROWN);
        roomManager.getRoom("casterly rock").addToStorage(Item.DAGGER);
        roomManager.getRoom("casterly rock").addToStorage(Item.POTION);
        roomManager.getRoom("riverrun").addToStorage(Item.GEM);
        roomManager.getRoom("riverrun").addToStorage(Item.MAGICBOOK);
        roomManager.getRoom("storms end").addToStorage(Item.SWORD);
        roomManager.getRoom("storms end").addToStorage(Item.FEATHER);
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
        if(!active){return;}

        printWelcome(); // output welcome message

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        // Start by checking if a Npc is in the starting room. If so, output appropriate message
        Npc.npcInRoom(currentRoom);
        // Flags indicating game status
        boolean finished = false;
        boolean won = false;
        while (!finished && !won) { // Loop whilst both variables are false

            Command command = parser.getCommand();  // Read users input
            finished = processCommand(command); // Process users input
            won = winCheck();   // Check if User has won the game
        }
        // when user wins or quits, this is set to false.
        // This stops the user from trying to continue playing the game after it has finished.
        active = false;
        System.out.println("\nThank you for playing!");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        //Terminal.doubleBar();
        //Terminal.doubleBar();
        System.out.println("Welcome to Westeros!");
        //Terminal.doubleBar();
        //Terminal.doubleBar();
        System.out.println();
        System.out.println("You are a Nobleman in Westeros, in the year 214 AC (After Conquest).");
        System.out.println("The realm is in turmoil, and the battle for power rages across the land.");
        System.out.println("The struggle for the Iron Throne has begun...");
        System.out.println("Will you claim it, or fall victim to the games of power?");
        System.out.println("The choice is yours, but beware...");
        System.out.println("In the game of thrones, you win or you die.\n");
        System.out.println("Type 'help' if you need help.");
        //Terminal.singleBar();
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
            System.out.println("\nInvalid command.");
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
        else if (commandWord.equals("search")) {
            search(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("back")) {
            back();
        }
        else if (commandWord.equals("drop")){
            dropItem(command);
        }
        else if(commandWord.equals("trade")){
            trade(command);
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
     * Try to in to one direction. If there is an unlocked exit.
     * If the exit is locked, try and unlock it.
     * Otherwise, print an error message.
     * @param command - contains the direction
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
            return;
        }

        // Check if the room can be unlocked
        if (nextRoom.isLocked()) {
            if (!canUnlock(nextRoom)){   // If room cannot be unlocked, return
                System.out.println("The room is locked...");
                System.out.println(nextRoom.getQuest());
                return;
            }
            // If room can be unlocked, unlock it.
            unlockRoom(nextRoom);
        }

        // Check if the room is the transporter room
        if (roomManager.checkTransporterRoom(nextRoom)){
            nextRoom = roomManager.getRandomRoom();
            // Check if the random room is locked
            // If so, check if user can unlock it
            while (nextRoom.isLocked()){
                // If they have not, generate a new room until one that can be accessed is found
                nextRoom = roomManager.getRandomRoom();
                // If room can be unlocked, unlock it.
                unlockRoom(nextRoom);
            }
            System.out.println("You found a portal and decided to step through " +
                    "and are being teleported to a new location...");
        }

        // move player into this room
        stack.push(currentRoom);    // Add current room to stack
        currentRoom = nextRoom;     // Move to next room
        System.out.println();
        System.out.println(currentRoom.getLongDescription());   // print description of room
        Npc.moveNpc();// Move NPCs to new room
        Npc.npcInRoom(currentRoom);

    }

    /**
     * Check if a room is locked.
     * @param nextRoom
     * @return true if room can be unlocked
     */
    private boolean canUnlock(Room nextRoom){
        List<Item> requirements = nextRoom.getRequiredItems();
        if (requirements.isEmpty()){return false;}
        for (Item item: requirements){
            if (!player.hasItem(item)){
                return false;
            }
        }
        return true;
    }

    /**
     * Remove required items to unlock the room from player's inventory.
     * Update the status of the room to unlocked.
     * @param nextRoom
     */
    private void unlockRoom(Room nextRoom){
        // Retrieve room storage and player inventory
        List<Item> requirements = nextRoom.getRequiredItems();
        // Remove required items from player inventory
        for (Item item: requirements){
            player.removeItem(item);
        }
        // Update room status to unlocked
        nextRoom.unlockRoom();

    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if(command.getSecondWord() != null) {
            System.out.println("Quit what?\n");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * "Search your inventory or search the room for items"
     * @param command - To check what user wants to search
     */
    private void search(Command command){
        if(!command.hasSecondWord()) {
            // If there is no second word we don't know what to search
            System.out.println("Search what?\n");
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("inventory")){
            // Display the items in the player's inventory.
            player.printInventory();
        }
        else if (command.getSecondWord().equalsIgnoreCase("room")){
            // Display the items in the room.
            currentRoom.showStorage();
        }
        else{
            // We don't know what to search
            System.out.println("Search what?\n");
        }
    }

    /**
     * Take an item from the room and add it to inventory if there is space.
     * If not, print an error message.
     * @param command
     */
    private void take(Command command){
        if(!command.hasSecondWord()) {
            // If there is no second word we don't know what to search
            System.out.println("Take what?");
            return;
        }

        // Try to retrieve the item from the room
        Item itemToTake = currentRoom.getItem(command.getSecondWord());

        if (itemToTake == null){
            System.out.println("Take what?");
            return;
        }
        else if (itemToTake.isLocked()){
            // Some items cannot be picked up
            System.out.println("Couldn't take this item");
            return;
        }
        // Check if there is enough space to fit the item in inventory.
        if (player.availableSpace() >= itemToTake.getWeight()){

            // If there is space in the inventory, the item is removed from
            // the room and added to the player's inventory
            player.addItem(currentRoom.removeItem(command.getSecondWord()));
        }
        else{
            System.out.println("Not enough space in inventory");
        }

    }

    /**
     * Method allowing user to go back to the previous room they came from
     * Utilizes stack data structure to store previously visited rooms in order.
     */
    private void back(){
        // Pop the previously visited room from the stack
        Room room = stack.pop();

        // Pop method will return null if there is no previous room to move to
        if (room == null){
            System.out.println("\nYou are in the room you started in...");
        }
        else{
            // player moves to previous room
            currentRoom = room;
            System.out.println(currentRoom.getLongDescription());
            Npc.moveNpc();// Move NPCs to new room
            Npc.npcInRoom(currentRoom);

        }
    }

    /**
     * Drop an item from inventory. Dropped item gets stored in room.
     * format - drop [item to drop]
     * @param command
     */
    private void dropItem(Command command){

        // Cannot drop an item if the inventory is empty
        // or if no item to drop is given
        if (!command.hasSecondWord()){
            System.out.println("Drop What?\n");
            return;
        } else if (player.invIsEmpty()) {
            System.out.println("Your inventory is empty...");
            return;
        }

        // Drop the item and add it to the room storage
        Item itemToDrop = Item.findByName(command.getSecondWord());
        if (itemToDrop == null){
            System.out.println("You don't have that item");
        }
        else{
            player.removeItem(itemToDrop); // Remove item from player inventory
            currentRoom.addToStorage(itemToDrop); // Add item top game storage

        }
    }



    private void trade(Command command){

        // Ensure that user input has second and third word
        if(!command.hasSecondWord() || !command.hasThirdWord()){
            System.out.println("Trade what?");
            return;
        }
        // Ensure that npc they wish to trade with is in the room
        Npc npc = Npc.getNpc(command.getSecondWord());

        // validate the npc
        if (!npcTradeValidation(npc)) return;

        if (command.getThirdWord().equalsIgnoreCase("offer")){
            npc.printInventory();
            return;
        }

        // The user command will contain the item they wish to receive.
        // check if the item name they have entered exists

        Item itemToReceive = Item.findByName(command.getThirdWord());
        if (itemToReceive == null){
            System.out.println("Trade what item?");
            return;
        }

        // Check if npc has that item
        if(!npc.hasItem(itemToReceive)){
            System.out.println(npc.getName() + " does not have this item");
            return;
        }

        // try to get the item the npc wants in return
        String itemToGiveName = itemToReceive.getTradeFor();
        if (itemToGiveName == null){
            System.out.println("You do not have anything " + npc.getName() +" wants...");
            return;
        }
        Item itemToGive = Item.findByName(itemToGiveName);

        // check if the player has that item
        if(!player.hasItem(itemToGive)){
            System.out.println("You do not have anything " + npc.getName() + " wants...");
            return;
        }

        // swap items and remove from inventories
        npc.removeItem(itemToReceive);
        npc.addItem(itemToGive);

        player.removeItem(itemToGive);

        // If player has enough space in inventory, add the item otherwise add it to room storage
        if (itemToReceive.getWeight() > player.availableSpace()){
            System.out.println("\nOpps! you don't have enough space in you inventory");
            System.out.println("The item was dropped somewhere in the room");
            currentRoom.addToStorage(itemToReceive);
        }
        else{
            player.addItem(itemToReceive);
        }

    }

    private boolean npcTradeValidation(Npc npc) {
        if (npc == null){
            System.out.println("Trade Who?");
            return false;
        } else if (!npc.getCurrentRoom().equals(currentRoom)) {
            System.out.println(npc.getName() + " is not at this location");
            return false;
        }
        return true;
    }


    /**
     * Method to check if the player has won the game.
     * In this game, the game is won if the player enters the 'iron throne' room.
     * The room is locked so they must complete the quest to unlock it first.
     * @return true if player has won the game
     */
    private boolean winCheck(){
        // If user is in iron throne room
        if (currentRoom.equals(roomManager.getRoom("iron throne"))){
            // Output victory messages
            System.out.println("VICTORY IS YOURS, YOU HAVE CLAIMED THE IRON THRONE!");
            System.out.println("WESTEROS IS NOW UNDER YOUR RULE!");
            System.out.println();
            System.out.print("but beware...\n");
            System.out.println("'sitting on a throne is a thousand times harder than winning one...' - King Robert Baratheon.");
            return true;    // Return true to end game loop
        }
        // return false to continue game loop
        return false;
    }

}
