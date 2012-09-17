package org.puzzle.rps;

import java.util.ArrayList;
import java.util.List;

import org.puzzle.rps.players.Player;

/**
 * Game engine.
 */
public class GameEngine {

  private Player player1;
  private Player player2;
  
  private RuleInterpreter ruleInterpreter;  
  private final int numRounds;
  private int roundDelay = 2000;
  
  private GameState gameState;
  
  private List<GameObserver> observers = new ArrayList<GameObserver>();
  
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
      
      notifyPlay(player1, play1);
      notifyPlay(player2, play2);
      
      Result res = ruleInterpreter.winner(play1, play2);

      round++;

      // Update scores..
      if (!res.winVerb.equals(Result.DRAW_VERB)) {
        // Check for winner/loser
        if (res.winningToken.equals(play1)) {
          gameState.p1Score++;
        } else {
          gameState.p2Score++;        
        }      
      }
      
      // Notify result of this round, then sleep for a bit
      notifyRoundOutcome(res);
      
      try {
        Thread.sleep(roundDelay);
      } catch (InterruptedException e) {
        throw new RuntimeException("Received unexpected interruption while sleeping in game...");
      }
    }
    
    // Notify the outcome of the game
    notifyGameOutcome(player1, gameState.p1Score, player2, gameState.p2Score);
    
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


  public Object registerObserver(GameObserver observer) {
    return observers.add(observer);
  }

  private void notifyPlay(Player p, String token) {
    for (GameObserver obs : observers) {
      obs.notifyPlay(p,token);
    }
  }

  private void notifyRoundOutcome(Result r) {
    for (GameObserver obs : observers) {
      obs.notifyRoundOutcome(r);
    }
  }

  private void notifyGameOutcome(Player p1, int p1score, Player p2, int p2score) {
    for (GameObserver obs : observers) {
      obs.notifyGameOutcome(p1, p1score, p2, p2score);
    }
  }

  
  public void setGameDelay(int delay) {
    roundDelay = delay;
  }
}
