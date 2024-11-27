public class Game {

    private GameManager gameManager;
    private boolean active = true;

    public Game(){
        gameManager = new GameManager();

    }


    public void play(){
        if(!active){return;}
        /*
        Do not start if game is inactive. This is to prevent a player calling the play method
        on the game a second time after they have already quit or won the game.
         */

        printWelcome(); // Display welcome message

        gameManager.displayNpcInfo();   // Display information about any npcs in starting room

        boolean finished = false;   // Flag to track if player quit
        boolean won = false;        // Flag to track if player won game

        while (!finished && !won) {
            finished = gameManager.checkFinished();
            won = gameManager.winCheck();
        }

        active = false; // Mark game as inactive
        System.out.println("\nThank you for playing!");

    }

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
        System.out.println(gameManager.getRoomLongDescription());
    }

}
