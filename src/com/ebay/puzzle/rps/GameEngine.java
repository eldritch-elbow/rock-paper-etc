package com.ebay.puzzle.rps;

/**
 * Game engine.
 */
public class GameEngine {

  private Player player1;
  private Player player2;
  
  private RuleInterpreter ruleInterpreter;  
  private final int numRounds;
  
  private GameState gameState;
  
  
  /**
   * Constructor for two players.
   * @param p1 Player one
   * @param p2 Player two
   * @param rules The rules to apply
   * @param numRounds Number of rounds to play
   */
  public GameEngine(Player p1, Player p2, RuleInterpreter rules, int rounds) {
    player1 = p1;
    player2 = p2;
    numRounds = rounds;
    ruleInterpreter = rules;
  }

  public void play() {
    
    // Prepare for a new game!
    gameState = new GameState();
    
    int round = 1;
    while (round <= numRounds) {  
      
      String play1 = player1.getMove();
      String play2 = player2.getMove(); 
      
      Result res = ruleInterpreter.winner(play1, play2);

      round++;

      // Check for a draw...
      if (res.winVerb.equals(Result.DRAW_VERB)) {
        continue;
      }
      
      // Check for winner/loser
      if (res.winningToken.equals(play1)) {
        gameState.p1Score++;
      } else {
        gameState.p2Score++;        
      }      
      
    }
  }

  public GameState outcome() {
    return gameState;
  }

  
  
  /**
   * Public static inner class for capturing and communicating GameState
   */
  public static class GameState {
    
    private int p1Score;
    private int p2Score;
    
    public int getP1Score() {
      return p1Score;
    }
    public int getP2Score() {
      return p2Score;
    }
    
  }
  
  
}
