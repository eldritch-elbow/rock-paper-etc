package org.puzzle.rps.players;

/**
 * Interface for Players. 
 * 
 * <p>Currently, there is only one method on this interface, for getting player moves.
 * A useful future enhancement will be to extend the GameObserver interface so that 
 * all game events are propagated to players. This will allow more sophisticated 
 * robots to be built.
 */
public interface Player {

  /**
   * Get the player's next move
   * @return String, one of the valid game tokens
   */
  public String getMove();

}
