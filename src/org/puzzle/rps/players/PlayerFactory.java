package org.puzzle.rps.players;

import java.util.List;

/** 
 * Factory for players.
 */
public class PlayerFactory {

  /**
   * An enum for player types. Currently just two types - Human, and Random Robot.
   * Possible extensions include heuristic based robots, networked humans etc.
   */
  public enum PlayerType {
    HUMAN, 
    RANDOM_COMPUTER
  }
  
  /**
   * Create an instance of the given player type, providing them with the 
   * given list of tokens.
   * @param type Instance of PlayerType
   * @param knownTokens List of tokens the player should use
   * @return
   */
  public Player createPlayer(PlayerType type, List<String> knownTokens) {
    
    switch (type) {
      case HUMAN:     // TODO refactor the UI code out of the human player, this is kind of ugly      
        return new HumanConsolePlayer(knownTokens, System.in, System.out);
      case RANDOM_COMPUTER:
        return new RandomComputerPlayer(knownTokens);
      default:
        throw new RuntimeException("Unexpected player type encountered");
    }
    
  }
  

}
