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
        // Create objects and create
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


        // Initialise rooms with descriptions, lcok status and key items. Add them to HashMap in RoomManager class
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

        // Add quests to rooms with locked exits.
        // Quests are only related to the story of the game, no additional functionality.
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

        addItems(); // Add items to rooms
        currentRoom = winterfell;  // Starting room for player
        stack.push(currentRoom);    // push first location to 'back' stack
        roomManager.setTransporterRoom(theWall); // Set transporter room
    }

    /**
     * Method adds items to specific rooms.
     */
    private void addItems(){
        // Retrieve the room from the HashMap of all rooms and add an item to it
        roomManager.getRoom("high garden").addToStorage(Item.COMPASS);
        roomManager.getRoom("dragon stone").addToStorage(Item.CROWN);
        roomManager.getRoom("casterly rock").addToStorage(Item.DAGGER);
        roomManager.getRoom("casterly rock").addToStorage(Item.POTION);
        roomManager.getRoom("riverrun").addToStorage(Item.GEM);
        roomManager.getRoom("riverrun").addToStorage(Item.MAGICBOOK);
        roomManager.getRoom("storms end").addToStorage(Item.SWORD);
        roomManager.getRoom("storms end").addToStorage(Item.FEATHER);
    }

    /**
     * Creates and initializes NPCs. Items are added to npc inventory
     */
    private void createNpc(){
        // Create two NPCs. Add their names, descriptions and starting locations
        Npc jon = new Npc("Jon", "Jon Snow, the son of Ned Stark", roomManager.getRoom("winterfell"));
        Npc merchant = new Npc("Merchant", "The merchant is a trader from Braavos. A mysterious island accross the Narrow Sea", roomManager.getRoom("riverrun"));
        jon.addItem(Item.GOLD); // Give Jon an item
        merchant.addItem(Item.DRAGONEGG);   // Give Merchant an item

        // Deciding which item to give each npc should be planned beforehand and should reflect enums in Items class.
        // This is to allow the game to flow smoothly and be possible to complete

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {
        if(!active){return;}
        /*
        Do not start if game is inactive. This is to prevent a player calling the play method
        on the game a second time after they have already quit or won the game.
         */

        printWelcome(); // Display welcome message

        Npc.npcInRoom(currentRoom); // Check if any npcs are in the starting room

        boolean finished = false;   // Flag to track if player quit
        boolean won = false;        // Flag to track if player won game

        while (!finished && !won) {
            Command command = parser.getCommand();  // Read users input
            finished = processCommand(command); // Process users input
            won = winCheck();   // Check if player has won the game
        }

        active = false; // Mark game as inactive
        System.out.println("\nThank you for playing!");
    }

    /**
     * Display game'sopening message
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Westeros!");
        System.out.println();
        System.out.println("You are a Nobleman in Westeros, in the year 214 AC (After Conquest).");
        System.out.println("The realm is in turmoil, and the battle for power rages across the land.");
        System.out.println("The struggle for the Iron Throne has begun...");
        System.out.println("Will you claim it, or fall victim to the games of power?");
        System.out.println("The choice is yours, but beware...");
        System.out.println("In the game of thrones, you win or you die.\n");
        System.out.println("Type 'help' to view game commands.");
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
        else if (commandWord.equals("go")) {    // Move to different room
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {  // Quit game
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("search")) {// Search room for items
            search(command);
        }
        else if (commandWord.equals("take")) {  // Take item from room
            take(command);
        }
        else if (commandWord.equals("back")) {  // Move to previous room
            back();
        }
        else if (commandWord.equals("drop")){   // Drop item from inventory
            dropItem(command);
        }
        else if(commandWord.equals("trade")){   // Trade item with npc
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
        System.out.println("You are an ambitious knight of the Seven Kingdoms");
        System.out.println("who wishes to be king.");
        System.out.println("Find a Sword, Crown and Dragon Egg to claim the throne.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to in to one direction. If there is an unlocked exit.
     * If the exit is locked, try and unlock it.
     * Otherwise, print an error message.
     * @param command - contains the direction the player wants to go
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
            // No exit in this direction
            System.out.println("There is no door!");
            return;
        }

        // Check if the room is locked
        if (nextRoom.isLocked()) {
            if (!canUnlock(nextRoom)){   // If room cannot be unlocked, return
                System.out.println("The room is locked...");
                // Print any additional information related to unlocking the room
                System.out.println(nextRoom.getQuest());
                return;
            }
            // If room can be unlocked, unlock it.
            unlockRoom(nextRoom);
        }

        // Check if the room is the transporter room (teleportation)
        if (roomManager.checkTransporterRoom(nextRoom)){
            // select a random room
            nextRoom = roomManager.getRandomRoom();

            // Check if the random room is locked

            while (nextRoom.isLocked()){
                // Find a new random room until an unlocked one is found

                nextRoom = roomManager.getRandomRoom();
                // Unlock the room if possible
                unlockRoom(nextRoom);
            }
            System.out.println("You found a portal and decided to step through " +
                    "and are being teleported to a new location...");
        }

        // move player into this room
        stack.push(currentRoom);    // Push current room to the stack (for backtracking)
        currentRoom = nextRoom;     // Move to next room
        System.out.println();
        System.out.println(currentRoom.getLongDescription());   // Print description of room
        Npc.moveNpc();// Move NPCs to new room
        Npc.npcInRoom(currentRoom); // Check and alert player of any npcs in the room

    }

    /**
     * Check if the room can be unlocked by the player based on required items.
     * @param nextRoom - the room to check
     * @return true if room can be unlocked
     */
    private boolean canUnlock(Room nextRoom){
        List<Item> requirements = nextRoom.getRequiredItems();  // Items needed to unlock
        if (requirements.isEmpty()){return false;}
        // If no items are required, the room cannot be unlocked

        // Check if the player has all the required items
        for (Item item: requirements){
            // If any are missing, the room cannot be unlocked
            if (!player.hasItem(item)){
                return false;
            }
        }
        return true;    // All required items are present, the room can be unlocked
    }

    /**
     * Unlock the room and remove required items from players inventory
     * Update the status of the room to unlocked.
     * @param nextRoom - The room to unlock
     */
    private void unlockRoom(Room nextRoom){
        // Retrieve list of items needed to unlock room
        List<Item> requirements = nextRoom.getRequiredItems();
        // Remove required items from player inventory
        for (Item item: requirements){
            player.removeItem(item);
        }
        // Mark room as unlocked
        nextRoom.unlockRoom();

    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if(command.getSecondWord() != null) {
            // If a second word was provided we don't want to quit
            System.out.println("Quit what?\n");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * "Allows player to view items available in their inventory or room"
     * @param command - Search action
     */
    private void search(Command command){
        if(!command.hasSecondWord()) {
            // If there is no second word we don't know what to search
            System.out.println("Search what?\n");
            return;
        }

        // Display the items in the player's inventory.
        if (command.getSecondWord().equalsIgnoreCase("inventory")){
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
     * @param command - command containing item to take
     */
    private void take(Command command){
        if(!command.hasSecondWord()) {
            // If there is no second word we don't know what to search
            System.out.println("Take what?");
            return;
        }

        // Retrieve item from room based on name
        Item itemToTake = currentRoom.getItem(command.getSecondWord());

        if (itemToTake == null){
            // Item doesnt exist in the room
            System.out.println("Take what?");
            return;
        }
        else if (itemToTake.isLocked()){
            // Item cannot be picked up because it's locked
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
            // If there's not enough space, inform the player
            System.out.println("Not enough space in inventory");
        }

    }

    /**
     * Method allowing user to go back to the previous room they came from
     * Utilizes stack data structure to store previously visited rooms in order.
     */
    private void back(){
        // Pop the last room form the stack
        Room room = stack.pop();

        // Pop method will return null if there is no previous room to move to
        if (room == null){
            System.out.println("\nYou are in the room you started in...");
        }
        else{
            // move the player back to the previous room
            currentRoom = room;
            System.out.println(currentRoom.getLongDescription());
            Npc.moveNpc();// Move NPCs to new room
            Npc.npcInRoom(currentRoom); // Update Npcs in the room

        }
    }

    /**
     * Drop an item from inventory. Dropped item gets stored in room.
     * format - drop [item to drop]
     * @param command - Contains the name of the item to drop
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


    /**
     * Method allowing the user to trade items with NPC characters.
     * Items that can be traded; and what they will be traded for
     * can be defined in the Items class. This class simply
     * implements the procedure of validating a trade and moving items
     * between inventories.
     * command format - trade [NPC name] [Item to give to npc]
     * @param command - Contains the npc to trade with and the item to trade
     */
    private void trade(Command command){

        // Ensure that user input has second and third word
        // otherwise we don't know who or what to trade.
        if(!command.hasSecondWord() || !command.hasThirdWord()){
            System.out.println("Trade what?");
            return;
        }
        // Get the npc to trade with
        Npc npc = Npc.getNpc(command.getSecondWord());

        // validate the npc
        if (!npcTradeValidation(npc)) return;

        // View which items the npc has in their inventory
        if (command.getThirdWord().equalsIgnoreCase("offer")){
            npc.printInventory();   // Output the npcs inventory
            return;
        }
        // Retrieve by name the item the player wants to receive from the trade
        Item itemToReceive = Item.findByName(command.getThirdWord());
        if (itemToReceive == null){
            // In the case that the player enters an invalid name
            System.out.println("Trade what item?");
            return;
        }

        // Check if item to receive form trade is present in npcs inventory
        if(!npc.hasItem(itemToReceive)){    // If npc doesnt have entered item
            System.out.println(npc.getName() + " does not have this item");
            return;
        }

        // Retrieve name of the item player must possess to trade
        String itemToGiveName = itemToReceive.getTradeFor();
        if (itemToGiveName == null){    // getTradeFore() returns null if item can't be traded
            System.out.println("You do not have anything " + npc.getName() +" wants...");
            return;
        }
        // If not null, retrieve the item by name
        Item itemToGive = Item.findByName(itemToGiveName);

        // check if the player has that item in inventory
        if(!player.hasItem(itemToGive)){
            // print error and return if player doesn't.
            System.out.println("You do not have anything " + npc.getName() + " wants...");
            return;
        }

        // Remove and add appropriate items to npc inventory
        npc.removeItem(itemToReceive);
        npc.addItem(itemToGive);

        // Remove appropriate item from player's inventory
        player.removeItem(itemToGive);

        // If player has enough space in inventory, add the item to inventory
        // otherwise add it to room storage
        if (itemToReceive.getWeight() > player.availableSpace()){
            System.out.println("\nOpps! you don't have enough space in you inventory");
            System.out.println("The item was dropped somewhere in the room");
            currentRoom.addToStorage(itemToReceive);
        }
        else{
            player.addItem(itemToReceive);
        }

    }

    /**
     * Class that validates the npc during the trading procedure. It ensures that the named
     * npc exists and is in the current room.
     * @param npc - The npc the player wishes to trade with
     * @return true if the npc exists and is int he player's room
     */
    private boolean npcTradeValidation(Npc npc) {
        // If player command contained invalid npc name
        if (npc == null){
            System.out.println("Trade Who?");
            return false;
        } else if (!npc.getCurrentRoom().equals(currentRoom)) {
            // If npc not in same room as player
            System.out.println(npc.getName() + " is not at this location");
            return false;
        }
        return true;
    }


    /**
     * Method to check if the player has won the game.
     * In this game, the game is won if the player enters the 'iron throne' room.
     * The Iron Throne room must first be unlocked
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
