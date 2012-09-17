package org.puzzle.rps;

import org.puzzle.rps.players.Player;

/**
 * Observer interface, for sending game events.
 */
public interface GameObserver {

  /** 
   * A play has taken place
   * @param player Who made the play
   * @param token Which token they played
   */
  void notifyPlay(Player player, String token);
  
  /**
   * Notify the outcome of the round
   * @param r Instance of Result, showing the result
   */
  void notifyRoundOutcome(Result r);

  /**
   * Notify the outcome of a game
   * @param player1 The first player in the game
   * @param p1Score Player one score 
   * @param player2 The second player in the game
   * @param p2Score Player two score
   */
  void notifyGameOutcome(Player player1, int p1Score, Player player2, int p2Score);

}
