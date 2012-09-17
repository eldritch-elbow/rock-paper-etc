package org.puzzle.rps.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;


/**
 * An object representing a human player. Provides simple console based I/O
 * to prompt a user for a game choice, and parse console input for game
 * play choices.
 * 
 * <p><i>Note:</i> The inclusion of console based interaction here is less
 * than ideal. I was considering refactoring the code to provide a cleaner
 * separation of UI versus input logic, but unfortunately ran out of time.
 */
public class HumanConsolePlayer implements Player {

  /* Ref: "Futurama: The Birdbot of Ice-Catraz" */
  private static String HUMAN_NAME = "Wiggles";
  
  private List<String> validTokens;
  private BufferedWriter writer;
  private BufferedReader reader;  
  
  /**
   * Public constructor
   * @param tokens List of all available tokens in play
   * @param is Stream from which to read user input
   * @param os Stream for writing output messages to
   */
  public HumanConsolePlayer(List<String> tokens, InputStream is, OutputStream os) {
    
    validTokens = tokens;
    
    // Prepare a reader and writer for character based IO
    reader = new BufferedReader(new InputStreamReader(is));
    writer = new BufferedWriter(new OutputStreamWriter(os));
  }

  @Override
  public String getMove() {

    // Prepare variable for user choice. Default is invalid to ensure programmatic 
    // errors are visible.
    int choiceIdx = -1;

    try {
            
      boolean validInput = false;
      while (!validInput) {
  
        // Prompt user for a move
        writer.write( String.format("Select your token %s [1-%d]: ", HUMAN_NAME, validTokens.size() ));
        writer.flush();

        // Read the human player's option from the console
        // TODO the ConsoleGamesMaster performs a similar task, refactor into common helper
        String choice = reader.readLine();
        
        // Parse it, then return the appropriate token.       
        try {
           choiceIdx = Integer.parseInt(choice) - 1;
           if (choiceIdx >= 0 && choiceIdx < validTokens.size()) {
             validInput = true;
           } 
        } catch (NumberFormatException e) {
          // User failed to input a valid integer
        }
        
        // Display a message if we have to try again
        if (!validInput) {
          writer.write( String.format("Invalid choice '%s', please try again!", choice.trim()) );
          writer.newLine();
          writer.flush();
        }
        
      }

    } catch (IOException e) {   
      // Any problems with IO - escalate to ensure visibility.
      throw new RuntimeException("IO Exception detected while requesting human player move.");
    } 

    return validTokens.get(choiceIdx);
  }

  @Override
  public String toString() {
    return HUMAN_NAME;
  }
  
  
}
