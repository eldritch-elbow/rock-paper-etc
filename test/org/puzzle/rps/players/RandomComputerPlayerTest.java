package org.puzzle.rps.players;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.puzzle.rps.players.Player;
import org.puzzle.rps.players.RandomComputerPlayer;


public class RandomComputerPlayerTest {

  // Test data - valid tokens that the computer can play
  List<String> validTokens;
  
  // Object under test: assign to required interface to force implementation
  Player randomCP;
  
  @Before 
  public void prep() {
    
    validTokens = new ArrayList<String>();
    validTokens.add("Sandwich");
    validTokens.add("New Zealand");
    validTokens.add("Chewbacca");
    
    randomCP = new RandomComputerPlayer(validTokens);
  }
  
  /*
   * The robot player can be asked for a move
   */
  @Test
  public void playerProvidesMove() {    
    assertTrue( validTokens.contains( randomCP.getMove() ) );    
  }

  /*
   * The moves *appear* to be random; this is sanity check more than anything
   */
  @Test
  public void movesAppearRandom() {
    
    // Actually quite hard to test properly. Just make sure it's
    // returning all tokens, at least.
    Set<String> found = new HashSet<String>();
    
    for (int i=0;i<1000;i++) {
      found.add( randomCP.getMove() );
      assertTrue( validTokens.contains( randomCP.getMove() ) );    
    }
    
    Set<String> expected = new HashSet<String>(validTokens);
    assertEquals(expected, found);    
  }

  /*
   * Two robot players, created at the same time, show different moves. 
   */
  @Test
  public void twoPlayersHaveDifferentMoves() {
    
    // Create two random players
    Player randomCP1 = new RandomComputerPlayer(validTokens);
    Player randomCP2 = new RandomComputerPlayer(validTokens);

    // Get 100 moves from each, and count how many times the moves differ
    int differCount=0;
    for (int i=0;i<100;i++) {
      String p1Move = randomCP1.getMove();
      String p2Move = randomCP2.getMove();
      if (!p1Move.equals(p2Move)) {
        differCount++;
      }      
    }
    
    // Check that they have differed at least once. Should be more 
    // but this is accurate.
    assertTrue(differCount > 0);
  }
 
  /*
   * Test that the robot has a name
   */
  @Test
  public void robbyTheRobot() {

    Player randomCP1 = new RandomComputerPlayer(validTokens);
    Player randomCP2 = new RandomComputerPlayer(validTokens);

    assertTrue(randomCP1.toString().startsWith("Robby "));
    assertTrue(randomCP2.toString().startsWith("Robby "));
    
  }
  

  
}
