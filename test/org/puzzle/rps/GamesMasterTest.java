package org.puzzle.rps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.puzzle.rps.GameEngine.GameState;
import org.puzzle.rps.players.Player;
import org.puzzle.rps.players.PlayerFactory;

public class GamesMasterTest {

  // Test data/classes
  RuleInterpreter testRules;
  
  // Mocks
  PlayerFactory playerFactory;
  Player p1;
  Player p2;
  GameEngine gameEngine;
  
  // Object under test: hold reference to test type, to allow access to test data
  TestGamesMaster gm;
  
  @Before
  public void prep() throws FileNotFoundException {
           
    /* Create the rule interpreter. For now, the expectation is that a games
     * master instance is always working with one set of rules hence the constructor. */
    testRules = new RuleInterpreter();
    testRules.parseRules( new File("src/resources/rpsRules.txt") );    
    
    /* Mock out player factory, players, and factory */ 
    playerFactory = Mockito.mock(PlayerFactory.class);
    p1 = Mockito.mock(Player.class);
    p2 = Mockito.mock(Player.class);
    gameEngine = Mockito.mock(GameEngine.class);
    
    /* Instantiate test instance of games master object. Using Template Method pattern 
     * to delegate UI specifics to subclass; assign to super-type var to ensure correct
     * interations */
    gm = new TestGamesMaster(testRules, playerFactory);
  }
  
  /*
   * Test that the GamesMaster calls it's abstract method for banner display 
   */
  @Test
  public void coreInteractionLoop_bannerShown() {
    
    // Invoke the GM in "single play" mode to check behaviour
    gm.runGames(false);    
    assertTrue(gm.bannerShown);
  }

  /*
   * Test that the GamesMaster prepares a game correctly 
   */
  @Test
  public void coreInteractionLoop_prepGame() {
    
    // Invoke GM, checking that a game is prepared
    gm.runGames(false);    
    assertEquals(p1, gm.preppedP1);
    assertEquals(p2, gm.preppedP2);
    assertEquals(7, gm.roundCount);
  }

  /*
   * Test that the GamesMaster starts it's prepared game 
   */
  @Test
  public void gameStarted() {
    
    GameState outcome = Mockito.mock(GameState.class);    
    when(gameEngine.outcome()).thenReturn(outcome);   
    
    gm.runGames(false);
    
    verify(gameEngine).play();
  }
  
  /*
   * Test that the GamesMaster passes on all game events to sub classes 
   */
  @Test
  public void updatesProvided() {
   
    gm.runGames(false);

    // Ensure that the game master registers itself for game events
    verify(gameEngine).registerObserver(gm);  
    
    // Also check that game events reach the test sub-class; i.e. the abstract
    // class doesn't get in the way
    Result resMock = Mockito.mock(Result.class);
    gm.notifyPlay(p2, "SomeToken");
    gm.notifyRoundOutcome(resMock);
    gm.notifyGameOutcome(p1, 17, p2, 32);
    
    assertEquals(resMock, gm.roundOutcome);
    assertEquals(p2, gm.playEventPlayer);
    assertEquals("SomeToken", gm.playEventToken);
    
    assertEquals(p1, gm.outcomeP1);
    assertEquals(p2, gm.outcomeP2);
    assertEquals(17, gm.outcomeP1score);
    assertEquals(32, gm.outcomeP2score);
  }

  
  
  /**
   * Games master sub-type, created for testing
   */
  private class TestGamesMaster extends GamesMaster {

    // Test state - records interactions from the super-type
    boolean bannerShown = false;    
    Player preppedP1 = null;
    Player preppedP2 = null;
    int roundCount = -1;    

    // Notification data
    Player playEventPlayer = null;
    String playEventToken = null;
    Result roundOutcome = null;    
    Player outcomeP1, outcomeP2 = null;
    int outcomeP1score, outcomeP2score = -1;
    
    public TestGamesMaster(RuleInterpreter ruleInterp, PlayerFactory playerFactory) {
      super(ruleInterp, playerFactory);
    }
    
    @Override
    protected void showBanner() {
      
      // Check that the rules are accessible to the sub class
      assertEquals(testRules, this.ruleInterpreter);
      bannerShown = true;
    }
    
    @Override
    protected Player readyPlayerOne(PlayerFactory factory) {
      return p1;
    }
    
    @Override
    protected Player readyPlayerTwo(PlayerFactory factory) {
      return p2;      
    }
    
    @Override
    protected int getRounds() {
      return 7;
    }
    
    @Override
    protected GameEngine prepareGame(Player p1, Player p2, int rounds) {
      
      preppedP1 = p1;
      preppedP2 = p2;
      roundCount = rounds;
      
      return gameEngine;
    }
    
    @Override
    public void notifyPlay(Player p, String t) {
      playEventPlayer = p;
      playEventToken = t;
    }

    @Override
    public void notifyRoundOutcome(Result r) {
      roundOutcome = r;
    }

    @Override
    public void notifyGameOutcome(Player p1, int p1score, Player p2, int p2score) {
      outcomeP1 = p1;
      outcomeP2 = p2;
      outcomeP1score = p1score;
      outcomeP2score = p2score;
    }

  }
  
  
}
