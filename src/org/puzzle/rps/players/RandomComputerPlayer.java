package org.puzzle.rps.players;

import java.util.List;
import java.util.Random;

public class RandomComputerPlayer implements Player {
  
  private static String NAME_PREFIX = "Robby";
  
  private String helloMyNameIs;   

  private Random random;
  private List<String> validTokens;
  private int tokenCount;
  
  public RandomComputerPlayer(List<String> tokens) {
    
    validTokens = tokens;
    tokenCount = validTokens.size();
    
    /* Get an instance of Random to control which tokens are returned.
     * Also sleep for a little while, to ensure that other instances of this
     * class (at least within the same thread) will generate different choices
     */ 
    random = new Random();
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException("Random computer player was unexpectedly woken from sleep...");
    }
    
    helloMyNameIs = String.format("%s %d", NAME_PREFIX, Math.abs( random.nextInt(10000) ));
  }

  @Override
  public String getMove() {

    int choice = random.nextInt(tokenCount);
    return validTokens.get(choice);
  }

  @Override
  public String toString() {
    return helloMyNameIs;
  }
  
}
