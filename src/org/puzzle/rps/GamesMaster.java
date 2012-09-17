package org.puzzle.rps;

import java.io.BufferedReader;

import org.puzzle.rps.players.Player;
import org.puzzle.rps.players.PlayerFactory;

public abstract class GamesMaster implements GameObserver {
  
  protected RuleInterpreter ruleInterpreter;
  protected PlayerFactory playerFactory;
  
  public GamesMaster(RuleInterpreter ruleInterp, PlayerFactory pFactory) {
    ruleInterpreter = ruleInterp;
    playerFactory = pFactory;
  }

  public void runGames(boolean continuous) {
    
    boolean runGame = true; // Always run at least one
    
    while (runGame) {      
      showBanner();
      
      Player p1 = readyPlayerOne(playerFactory);
      Player p2 = readyPlayerTwo(playerFactory);
      
      int rounds = getRounds();
      
      GameEngine game = prepareGame(p1,p2,rounds);
      game.registerObserver(this);
      
      game.play();

      /* That's the last step: now all game events will be notified
       * from the game engine */ 
      
      runGame = continuous ? true : false;
    }
    
  }

  protected abstract void showBanner();

  protected abstract Player readyPlayerOne(PlayerFactory factory);

  protected abstract Player readyPlayerTwo(PlayerFactory factory);

  protected abstract int getRounds();

  protected abstract GameEngine prepareGame(Player p1, Player p2, int rounds);


  
}
