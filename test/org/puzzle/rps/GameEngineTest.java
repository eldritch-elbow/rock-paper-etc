package org.puzzle.rps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.puzzle.rps.GameEngine.GameState;
import org.puzzle.rps.players.Player;



public class GameEngineTest {

  // Mocks
  Player p1Mock;
  Player p2Mock;
  
  // Instance of class under test
  GameEngine gameEngine;
  
  @Before
  public void prep() throws FileNotFoundException {
    
    // Prepare default mocks and behaviour
    p1Mock = Mockito.mock(Player.class);
    p2Mock = Mockito.mock(Player.class);
    when(p1Mock.getMove()).thenReturn("Rock");
    when(p2Mock.getMove()).thenReturn("Rock");

    // The rules to use while testing. This is a slightly higher level test but the 
    // interface of the RI is simple, and it's behaviour quite transparent.
    
    RuleInterpreter ri = new RuleInterpreter();
    ri.parseRules(new File("test/resources/rpsRules.txt"));
    
    // Instance of the game engine, with given rules; override delay for speedy tests
    gameEngine = new GameEngine(p1Mock,p2Mock,ri,3);
    gameEngine.setGameDelay(10);
  }
  
  @Test
  public void gameAsksPlayersForMoves() {

    gameEngine.play();
    
    verify(p1Mock, atLeastOnce()).getMove();
    verify(p2Mock, atLeastOnce()).getMove();    
  }

  @Test
  public void gameAsksPlayersForRightNumberOfMoves() {

    gameEngine.play();
    
    verify(p1Mock, times(3)).getMove();
    verify(p2Mock, times(3)).getMove();    
  }

  @Test
  public void rulesApplied_PlayerTwoWins() {

    // Define moves such that player two wins...
    when(p1Mock.getMove()).thenReturn("Rock","Scissors","Rock");
    when(p2Mock.getMove()).thenReturn("Rock","Rock","Paper");

    // Play the game...
    gameEngine.play();
    
    // Check that player two won
    GameState outcome = gameEngine.outcome();
    assertEquals(0, outcome.getP1Score());
    assertEquals(2, outcome.getP2Score());
  }

  @Test
  public void rulesApplied_PlayerOneWins() {

    // Define moves such that player two wins...
    when(p1Mock.getMove()).thenReturn("Scissors","Paper","Rock");
    when(p2Mock.getMove()).thenReturn("Paper","Rock","Scissors");

    // Play the game...
    gameEngine.play();
    
    // Check that player one won
    GameState outcome = gameEngine.outcome();
    assertEquals(3, outcome.getP1Score());
    assertEquals(0, outcome.getP2Score());
  }
  
  @Test
  public void rulesApplied_ItsADraw() {

    // Define moves such that player two wins...
    when(p1Mock.getMove()).thenReturn("Scissors","Scissors","Rock");
    when(p2Mock.getMove()).thenReturn("Paper","Rock","Rock");

    // Play the game...
    gameEngine.play();
    
    // Check that player one won
    GameState outcome = gameEngine.outcome();
    assertEquals(1, outcome.getP1Score());
    assertEquals(1, outcome.getP2Score());
  }

  @Test
  public void rulesApplied_lizardGame_P1WinsOverSevenRounds() throws FileNotFoundException {

    RuleInterpreter ri = new RuleInterpreter();
    ri.parseRules(new File("test/resources/lizardSpockRules.txt"));
    
    // Instance of the game engine, with given rules
    gameEngine = new GameEngine(p1Mock,p2Mock,ri,7);
    gameEngine.setGameDelay(10);

    // Define moves such that player two wins...
    when(p1Mock.getMove()).thenReturn("Spock","Lizard","Paper","Paper","Scissors","Lizard","Spock");
    when(p2Mock.getMove()).thenReturn("Rock","Paper","Scissors","Spock","Rock","Rock","Scissors");

    // Play the game...
    gameEngine.play();
    
    // Check that player one won
    GameState outcome = gameEngine.outcome();
    assertEquals(4, outcome.getP1Score());
    assertEquals(3, outcome.getP2Score());
  }
  

  @Test
  public void roundsNotified() throws FileNotFoundException {

    RuleInterpreter ri = new RuleInterpreter();
    ri.parseRules(new File("test/resources/lizardSpockRules.txt"));

    // Mock out a game observer, register it with the engine
    GameObserver gObserver = Mockito.mock(GameObserver.class);
    gameEngine = new GameEngine(p1Mock,p2Mock,ri,3);
    gameEngine.setGameDelay(10);
    gameEngine.registerObserver( gObserver );

    // Define moves such that player two wins...
    when(p1Mock.getMove()).thenReturn("Spock","Lizard","Paper");
    when(p2Mock.getMove()).thenReturn("Rock","Paper","Paper");

    // Play the game...
    gameEngine.play();

    // Check that the right events were notified
    InOrder inOrder = Mockito.inOrder(gObserver);
    inOrder.verify(gObserver).notifyPlay(p1Mock, "Spock");
    inOrder.verify(gObserver).notifyPlay(p2Mock, "Rock");
    inOrder.verify(gObserver).notifyRoundOutcome(new Result("Spock","vaporizes","Rock"));
    inOrder.verify(gObserver).notifyPlay(p1Mock, "Lizard");
    inOrder.verify(gObserver).notifyPlay(p2Mock, "Paper");
    inOrder.verify(gObserver).notifyRoundOutcome(new Result("Lizard","eats","Paper"));
    inOrder.verify(gObserver).notifyPlay(p1Mock, "Paper");
    inOrder.verify(gObserver).notifyPlay(p2Mock, "Paper");
    inOrder.verify(gObserver).notifyRoundOutcome(new Result("Paper","draws with","Paper"));
    inOrder.verify(gObserver).notifyGameOutcome(p1Mock, 2, p2Mock, 0);
  }
  
  /*
   * This isn't technically a Game Engine test; it's a simple enough test
   * of the Result class, and isn't particularly out of place here
   */
  @Test
  public void resultStringGenerated() {
    Result testRes = new Result("Paper","disproves","Spock");
    assertEquals("Paper disproves Spock", testRes.toString());
  }
  
}
