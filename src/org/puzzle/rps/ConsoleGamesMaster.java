package org.puzzle.rps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.puzzle.rps.players.Player;
import org.puzzle.rps.players.PlayerFactory;
import org.puzzle.rps.players.PlayerFactory.PlayerType;
import org.puzzle.rps.support.FileSupport;

/**
 * A console based games master. Extends GamesMaster, and implements all methods
 * using the system console, to provide a means for the user to select players
 * and potentially participate in games.
 * 
 * <p><i>Note:</i> this is the only class omitted from unit tests; UI logic is notoriously
 * tricky to unit test. However the the abstract logic provided in the GamesMaster does have
 * unit test coverage.
 * 
 * <p>Also this class does not currently provide a means for changing rulesets between
 * games. The rule set can be provided via a construction parameter.
 * 
 * ASCII art dinosaurs courtesy:
 * http://www.ascii-art.de/ascii/def/dinosaur.txt
 */
public class ConsoleGamesMaster extends GamesMaster {

  private static final String ASCII_BANNER;
  private static final String[] DINOSAURS;  
  private static final Random RANDOM_DINO = new Random();
  
  private BufferedReader reader;  
  private List<String> tokenList;
  
  // Static initializer, for reading in file based resources
  static {
    try {
      ASCII_BANNER = FileSupport.getStringFromFile("src/resources/banner.txt");
      
      /* Read in dinosaurs. A little clumsy ... would be nice to refactor
       * so that an arbitrary number of dinosaurs can be read. 
       */
      DINOSAURS = new String[5];
      DINOSAURS[0] = FileSupport.getStringFromFile("src/resources/dinosaur1.txt");
      DINOSAURS[1] = FileSupport.getStringFromFile("src/resources/dinosaur2.txt");
      DINOSAURS[2] = FileSupport.getStringFromFile("src/resources/dinosaur3.txt");
      DINOSAURS[3] = FileSupport.getStringFromFile("src/resources/dinosaur4.txt");
      DINOSAURS[4] = FileSupport.getStringFromFile("src/resources/dinosaur5.txt");
      
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Unable to read ASCII Art! Please ensure program is being run from the project root...");
    }
  }
  
  /**
   * Public constructor
   * @param ruleInterp The rules to apply to games
   * @param pFactory Class for creating players
   */
  public ConsoleGamesMaster(RuleInterpreter ruleInterp, PlayerFactory pFactory) {
    super(ruleInterp, pFactory);
        
    // Prepare console resource for reading input
    reader = new BufferedReader(new InputStreamReader(System.in));
    
    // Create a token list for display, and for player creation
    tokenList = new ArrayList<String>();
    tokenList.addAll( ruleInterpreter.getTokens() );
  }

  @Override
  protected void showBanner() {

    // Show a nice ASCII banner, then make a reference to a classic movie
    System.out.println(ASCII_BANNER);
    
    System.out.println();    
    System.out.println("SHALL WE PLAY A GAME?");    
    System.out.println();    
    
    // Display available game tokens and their associated numbers
    System.out.println("Game tokens:");
    for (int j=0; j<tokenList.size(); j++ ) {
      System.out.println( String.format("  %d - %s", j+1, tokenList.get(j)) );
    }
    
    // Display available player types
    System.out.println("Available player types: ");
    
    PlayerType[] types = PlayerType.values();    
    for (int i=0; i<types.length; i++ ) {
      System.out.println( String.format("  %d - %s", i+1, types[i]) );
    }

    System.out.println();    

  }

  @Override
  protected Player readyPlayerOne(PlayerFactory factory) {
    return readyPlayer("Select player 1 type: ", factory);
  }

  @Override
  protected Player readyPlayerTwo(PlayerFactory factory) {
    return readyPlayer("Select player 2 type: ", factory);
  }

  /*
   * Private helper method. Display the given console prompt, then
   * obtains a choice of player and creates the required type using
   * the player factory. 
   */
  private Player readyPlayer(String consolePrompt, PlayerFactory factory) {
    
    System.out.print(consolePrompt);
    
    int choice = readIntFromConsole( 1, PlayerType.values().length );
    PlayerType type = PlayerType.values()[choice-1];
    
    return factory.createPlayer( type , tokenList );
  }


  @Override
  protected int getRounds() {

    // Display console prompt, retrieve user choice
    System.out.print("Enter a number of rounds to play: ");
    int rounds = readIntFromConsole( 1, Integer.MAX_VALUE );

    return rounds;
  }

  @Override
  protected GameEngine prepareGame(Player p1, Player p2, int rounds) {
    
    System.out.println();
    System.out.println("OK. LET'S PLAY A GAME");

    // Straight pass through to GameEngine constructor
    return new GameEngine(p1,p2,ruleInterpreter,rounds);
  }

  @Override
  public void notifyPlay(Player player, String token) {
    // A player made a move! Display it.
    System.out.println( String.format("Player [%s] plays '%s'", player, token) );
  }
  
  @Override
  public void notifyRoundOutcome(Result res) {
    // A round is complete! Display the result
    System.out.println( res );
    System.out.println();    
  }

  @Override
  public void notifyGameOutcome(Player p1, int p1score, Player p2, int p2score) {
    
    // Game over. Display the score, and output the winner if there is one.
    
    System.out.println( "Final score:" );
    System.out.println( String.format("%s: %d", p1, p1score )); 
    System.out.println( String.format("%s: %d", p2, p2score )); 

    if (p1score > p2score) {
      outputWinner(p1.toString());
    } else if (p2score > p1score) {
      outputWinner(p2.toString());
    } else {
      System.out.println("It's a draw! No dinosaurs this time.");
      System.out.println();
    }
  }

  private void outputWinner(String winner) {
    
    // Display the winner, then a random choice of dinosaur
    System.out.println( String.format( "And the winner is: %s", winner) );
    System.out.println( "" );  
    System.out.println( String.format( "Congratulations %s, here is your celebratory dinosaur:",winner ));
    System.out.println( "" );  
    
    System.out.println( DINOSAURS[ RANDOM_DINO.nextInt( DINOSAURS.length ) ] );
  }

  
  /*
   * Private helper method. Reads an int value from the console, ensuring that 
   * it is well formed and falls within the required range.
   * @param min Min input value (inc)
   * @param max Max input value (inc)
   * @return int value, the user's choice
   */
  private int readIntFromConsole(int min, int max) {
    
    int choiceVal = -1;
    
    try {
      
      boolean validInput = false;
      while (!validInput) {

        // Read in the user's choice, parse it, make sure it's in good order
        String choice = reader.readLine();
        
        try {        
           choiceVal = Integer.parseInt(choice);
           if (choiceVal >= min && choiceVal <= max) {
             validInput = true;
           } 
        } catch (NumberFormatException e) {
          // User failed to input a valid integer
        }
  
        // Display a message if we have to try again
        if (!validInput) {
          System.out.println( String.format("Invalid choice '%s', please try again!", choice.trim()) );
        }
        
      }
    
    } catch (IOException e) {
      throw new RuntimeException("IO Exception detected while reading user input...");
    }
    
    return choiceVal;

  }

  
  
  /**
   * Main method for executing the console based games master.
   * @param args Input arguments
   * @throws FileNotFoundException If the string given by parameter 1 does not correspond to a file 
   */
  public static void main(String args[]) throws FileNotFoundException {
    
    if (args.length != 1) {
      System.out.println("Usage: java [jvm args] org.puzzle.rps.ConsoleGamesMaster [rule file]");
    }
    
    // Use rules defined on command line, plus default player factory
    RuleInterpreter ruleset = new RuleInterpreter();
    ruleset.parseRules( new File(args[0]) );
    
    PlayerFactory playerFactory = new PlayerFactory();
    
    // Create instance of this class, set it going...
    ConsoleGamesMaster gm = new ConsoleGamesMaster(ruleset, playerFactory);
    gm.runGames(true);
  }
  

}
