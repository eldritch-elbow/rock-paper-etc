package org.puzzle.rps;

import org.puzzle.rps.players.Player;
import org.puzzle.rps.players.PlayerFactory;

/**
 * Class for running a collection of different games. Currently
 * supports a single ruleset, but choices of different players each game.
 * 
 * <p>This is an abstract class to provide a level of decoupling between
 * the game master logic and user interaction. The runGames() method
 * provides the abstract logic for running a series of games, while the 
 * different abstract methods should be implemented by sub-types to get user
 * choices and provide game feedback.
 * 
 * <p>Once a game has started, all gameplay events are communicated via the 
 * GameObserver interface. 
 */
public abstract class GamesMaster implements GameObserver {
  
  /** Rule interpreter, should be used during game creation */
  protected RuleInterpreter ruleInterpreter;
  /** The player factory, provides a way for the different types of players to be created */
  protected PlayerFactory playerFactory;
  
  /**
   * Public constructor
   * @param ruleInterp The set of rules to use
   * @param pFactory A factory for creating players
   */
  public GamesMaster(RuleInterpreter ruleInterp, PlayerFactory pFactory) {
    ruleInterpreter = ruleInterp;
    playerFactory = pFactory;
  }

  /**
   * Run one or more games
   * @param continuous If true, keep playing games forever. Otherwise, just play one game
   */
  public void runGames(boolean continuous) {
    
    boolean runGame = true; // Always run at least one
    
    while (runGame) {
      
      /* This is the core logic for running a single game, from showing the initial banner,
       * through creating players and games, to receiving game events.
       */
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

  /**
   * Implement this method to display any initial banner before each game
   */
  protected abstract void showBanner();

  /**
   * Called to get the first player. The PlayerFactory is provided for convenience;
   * it's also available as a protected variable.
   * @return Instance of Player for player 1
   */
  protected abstract Player readyPlayerOne(PlayerFactory factory);

  /**
   * Called to get the second player. The PlayerFactory is provided for convenience;
   * it's also available as a protected variable.
   * @return Instance of Player for player 2
   */
  protected abstract Player readyPlayerTwo(PlayerFactory factory);

  /**
   * Get the number of rounds to play
   * @return Number of rounds for this game
   */
  protected abstract int getRounds();

  /**
   * Prepare a game, given the various game parameters. 
   * @param p1 The first player
   * @param p2 The second player
   * @param rounds Number of rounds to play
   * @return Instance of GameEngine, which can be used to execute the game
   */
  protected abstract GameEngine prepareGame(Player p1, Player p2, int rounds);


  
}
