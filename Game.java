import java.util.ArrayList;
import java.util.List;
// TODO implement finditembyname method in certain places.
// TODO Take another look at back class.
// TODO report

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
        ArrayList<Item> playerInv = player.getInventory();
        // Remove required items from player inventory
        for (Item item: requirements){
            playerInv.remove(item);
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

        if (command.getSecondWord().toLowerCase().equals("inventory")){
            // Display the items in the player's inventory.
            player.printInventory();
        }
        else if (command.getSecondWord().toLowerCase().equals("room")){
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
            System.out.println("Take what?\n");
            return;
        }

        // Try to retrieve the item from the room
        Item itemToTake = currentRoom.getItem(command.getSecondWord());

        if (itemToTake == null){
            System.out.println("Take what?\n");
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
        }
    }

    /**
     * Drop an item from inventory. Dropped item gets stored in room.
     * format - drop [item to drop]
     * @param command
     */
    private void dropItem(Command command){
        ArrayList<Item> playerInv = player.getInventory();

        // Cannot drop an item if the inventory is empty
        // or if no item to drop is given
        if (playerInv.isEmpty() || !command.hasSecondWord()){
            return;
        }

        // Drop the item and add it to the room storage
        for (Item item: playerInv){
            if (item.getName().equalsIgnoreCase(command.getSecondWord())){
                playerInv.remove(item); // Remove item from player inventory
                currentRoom.addToStorage(item); // Add item top game storage
            }
        }
    }

    private void trade(Command command){
        // Cannot trade if no player or item is named
        if(!command.hasSecondWord() || !command.hasThirdWord()){
            System.out.println("Trade what?");
            return;
        }
        // Get NPC to trade with
        Npc npc = Npc.getNpc(command.getSecondWord());


        if(npc == null){
            System.out.println("Trade who?");
            return;
        }

        ArrayList<Item> playerInv = player.getInventory();
        ArrayList<Item> NpcInv = npc.getInventory();

        // player may view the NPCs trade offer by using command 'trade [NPC name] offer'.
        if (command.getThirdWord().equals("offer")) {
            npc.printInventory();
            return;
        }   // Check if the player has the item they wish to trade in their inventory

        Item itemToTrade = Item.findByName(command.getThirdWord());

        if (itemToTrade == null || !playerInv.contains(itemToTrade)){
            // If user does not have the item they wish to trade, print error message.
            System.out.println("you dont have that");
            return;
        }
        else if (NpcInv.isEmpty()) {
            System.out.println(npc.getName() + " Has nothing to offer...");
            return;
        }
        String itemToReceiveName = itemToTrade.getTradeFor();
        Item itemToReceive = null;

        if(itemToReceiveName == null){
            System.out.println(npc.getName() + " doesn't want that item!");
            return;
        }
        else{
            try{
                itemToReceive = Item.findByName(itemToReceiveName);
            } catch (Exception e) {System.out.println("Error trading. Item not initialised correctly!!");}
        }

        // Conduct trade
        assert itemToReceive != null;
        NpcInv.remove(itemToReceive);
        NpcInv.add(itemToTrade);
        if (player.availableSpace() >= itemToReceive.getWeight()){

            player.addItem(itemToReceive);
        }
        else{
            System.out.println("Not enough space in inventory");
            System.out.println("Item dropped somewhere in room");
            currentRoom.addToStorage(itemToReceive);
        }

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
