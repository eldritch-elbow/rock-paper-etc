package com.ebay.puzzle.rps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RuleInterpreterTest {

  RuleInterpreter ri;
  
  @Before 
  public void prep() {
    // Create instance of class under test
    ri = new RuleInterpreter();
  }
  
  /*
   * Test that a simple rule can be parsed, and the tokens it contains returned
   * from the interpreter.
   */
  @Test
  public void canParseTokens() throws FileNotFoundException {

    ri.parseRules(new File("test/resources/simpleRule.txt"));

    Set<String> tokens = ri.getTokens();

    // Check returned value (use anon subclass idiom for Set init)
    Set<String> expected = new HashSet<String>();
    expected.add("Rock");
    expected.add("Scissors");

    assertEquals(2, tokens.size());
    assertEquals(expected, tokens);
  }

  /*
   * Verify that rules have been interpreted and can be consulted
   * by the client.
   */
  @Test
  public void canCheckRules() throws FileNotFoundException {
   
    ri.parseRules(new File("test/resources/simpleRule.txt"));

    // Now check that Rock does indeed beat scissors
    Result expRes = new Result("Rock", "crushes", "Scissors");
    assertEquals(expRes, ri.winner("Rock","Scissors"));
    assertEquals(expRes, ri.winner("Scissors","Rock"));
  }

  /*
   * Check that multiple rules are read and interpreted.
   */
  @Test
  public void canCheckManyRules() throws FileNotFoundException {
   
    ri.parseRules(new File("test/resources/rpsRules.txt"));

    // Check all winning/losing states...
    checkOutcome("Rock","Scissors", new Result("Rock", "crushes", "Scissors"));
    checkOutcome("Paper","Scissors", new Result("Scissors", "cuts", "Paper"));
    checkOutcome("Rock","Paper", new Result("Paper", "covers", "Rock"));

    // Check all draw states
    checkOutcome("Rock","Rock", new Result("Rock", "draws with", "Rock"));;
    checkOutcome("Scissors","Scissors", new Result("Scissors", "draws with", "Scissors"));
    checkOutcome("Paper","Paper", new Result("Paper", "draws with", "Paper"));    
  }

  /*
   * Check that multiple arbitrary rules are read and interpreted.
   */
  @Test
  public void threeArbitraryRules() throws FileNotFoundException {
   
    ri.parseRules(new File("test/resources/rangerRules.txt"));

    // Check all winning/losing states...
    checkOutcome("Ranger","Poacher", new Result("Ranger", "arrests", "Poacher"));
    checkOutcome("Poacher","Bear", new Result("Poacher", "shoots", "Bear"));
    checkOutcome("Bear","Ranger", new Result("Bear", "mauls", "Ranger"));

    // Check all draw states
    checkOutcome("Ranger","Ranger", new Result("Ranger", "draws with", "Ranger"));;
    checkOutcome("Poacher","Poacher", new Result("Poacher", "draws with", "Poacher"));
    checkOutcome("Bear","Bear", new Result("Bear", "draws with", "Bear"));    
  }

  /*
   * Check a more complex scenario: RPS + lizard, spock
   */
  @Test
  public void manyManyManyArbitraryRules() throws FileNotFoundException {
   
    ri.parseRules(new File("test/resources/lizardSpockRules.txt"));

    // Check all win states...
    checkOutcome("Scissors","Paper",    new Result("Scissors", "cuts", "Paper"));
    checkOutcome("Paper","Rock",        new Result("Paper", "covers", "Rock"));
    checkOutcome("Rock","Lizard",       new Result("Rock", "crushes", "Lizard"));
    checkOutcome("Lizard","Spock",      new Result("Lizard", "poisons", "Spock"));
    checkOutcome("Spock","Scissors",    new Result("Spock", "smashes", "Scissors"));
    checkOutcome("Scissors","Lizard",   new Result("Scissors", "decapitates", "Lizard"));
    checkOutcome("Lizard","Paper",      new Result("Lizard", "eats", "Paper"));
    checkOutcome("Paper","Spock",       new Result("Paper", "disproves", "Spock"));
    checkOutcome("Spock","Rock",        new Result("Spock", "vaporizes", "Rock"));
    checkOutcome("Rock","Scissors",     new Result("Rock", "crushes", "Scissors"));
    
    // Check all draw states...
    checkOutcome("Rock","Rock",         new Result("Rock", "draws with", "Rock"));
    checkOutcome("Scissors","Scissors", new Result("Scissors", "draws with", "Scissors"));
    checkOutcome("Paper","Paper",       new Result("Paper", "draws with", "Paper"));    
    checkOutcome("Lizard","Lizard",     new Result("Lizard", "draws with", "Lizard"));    
    checkOutcome("Spock","Spock",       new Result("Spock", "draws with", "Spock"));    

  }
  
  /*
   * Expect a severe exception for malformed rules. In production
   * it might make more sense to throw a checked exception.
   */
  @Test
  public void invalidRuleRejected() throws FileNotFoundException {
    
    try {
      ri.parseRules(new File("test/resources/illegalRule.txt"));
      fail("RTE expected");
    } catch (RuntimeException e) {
      assertEquals("Malformed rule detected", e.getMessage());
    }
    
  }

  /*
   * Check for invalid input tokens...
   */
  @Test
  public void invalidRuleCheckRejected() throws FileNotFoundException {

    ri.parseRules(new File("test/resources/rpsRules.txt"));

    try {
      ri.winner("Bicycle", "Doctor Who");
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid game Token 'Bicycle'", e.getMessage());
    }

    try {
      ri.winner("Rock", "Doctor Who");
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid game Token 'Doctor Who'", e.getMessage());
    }

  }
  
  
  
  private void checkOutcome(String t1, String t2, Result expResult) {
    
    assertEquals(expResult, ri.winner(t1,t2));
    assertEquals(expResult, ri.winner(t2,t1));  
    
  }


  
  
}
