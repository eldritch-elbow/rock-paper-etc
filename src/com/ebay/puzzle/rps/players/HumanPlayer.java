package com.ebay.puzzle.rps.players;

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
 */
public class HumanPlayer implements Player {

  private List<String> validTokens;
  private BufferedWriter writer;
  private BufferedReader reader;  
  
  public HumanPlayer(List<String> tokens, InputStream is, OutputStream os) {
    
    validTokens = tokens;
    
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
  
        writer.write( String.format("Select your token [1-%d]: ", validTokens.size() ));
        writer.newLine();
        writer.flush();

        // Read the human player's option from the console
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
      throw new RuntimeException("IO Exception detected while requesting human player move.");
    } 

    return validTokens.get(choiceIdx);
  }

}
