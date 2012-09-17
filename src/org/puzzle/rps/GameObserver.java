package org.puzzle.rps;

import org.puzzle.rps.players.Player;

public interface GameObserver {

  void notifyPlay(Player player, String token);
  
  void notifyRoundOutcome(Result r);

  void notifyGameOutcome(Player player1, int winScore, Player player2, int loseScore);

}
