package org.puzzle.rps.players;

import java.util.List;

public class PlayerFactory {

  public enum PlayerType {
    HUMAN, 
    RANDOM_COMPUTER
  }
  
  public Player createPlayer(PlayerType type, List<String> knownTokens) {
    
    switch (type) {
      case HUMAN:     // TODO refactor the UI code out of the human player - this needs work      
        return new HumanConsolePlayer(knownTokens, System.in, System.out);
      case RANDOM_COMPUTER:
        return new RandomComputerPlayer(knownTokens);
      default:
        throw new RuntimeException("Unexpected player type encountered");
    }
    
  }
  

}
