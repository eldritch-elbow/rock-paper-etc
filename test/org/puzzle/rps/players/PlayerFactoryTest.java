package org.puzzle.rps.players;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.puzzle.rps.players.PlayerFactory.PlayerType;

public class PlayerFactoryTest {

  // Test data
  List<String> tokens;
  
  // Instance under test
  PlayerFactory pf;
  
  @Before
  public void prep() {
    tokens = new ArrayList<String>();
    tokens.add("Sofa");
    tokens.add("Hollywood");
    tokens.add("Turntable");
    pf = new PlayerFactory();
  }
  
  @Test
  public void checkPlayerEnum() {
    
    PlayerType[] knownTypes = PlayerType.values();
    assertEquals(2, knownTypes.length);
    assertEquals(PlayerType.HUMAN, knownTypes[0]);
    assertEquals(PlayerType.RANDOM_COMPUTER, knownTypes[1]);
  }

  /*
   * Check that all known player types are correctly created.
   */
  @Test
  public void createPlayerTypes() {
    
    Player human = pf.createPlayer(PlayerType.HUMAN, tokens);
    assertTrue(human instanceof HumanConsolePlayer);

    Player randomComp = pf.createPlayer(PlayerType.RANDOM_COMPUTER, tokens);
    assertTrue(randomComp instanceof RandomComputerPlayer);

  }

  /* Creational logical is tricky to test ... this is more of a sanity
   * check that one of the player types has been instantiated with the 
   * given tokens. */
  @Test
  public void tokensPassed() {

    Player randomComp = pf.createPlayer(PlayerType.RANDOM_COMPUTER, tokens);
    
    Set<String> expected = new HashSet<String>(tokens);
    for (int i=0; i<10; i++) {
      String move = randomComp.getMove();
      assertTrue(expected.contains(move));      
    }

  }

  
}
