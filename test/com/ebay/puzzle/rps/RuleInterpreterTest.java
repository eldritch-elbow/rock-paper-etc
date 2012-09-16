package com.ebay.puzzle.rps;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class RuleInterpreterTest {

  /*
   * Test that a simple rule can be parsed, and the tokens it contains returned
   * from the interpreter.
   */
  @Test
  public void canParseTokens() throws FileNotFoundException {

    File simpleRuleFile = new File("test/resources/simpleRule.txt");

    RuleInterpreter ri = new RuleInterpreter();
    ri.parseRules(simpleRuleFile);

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
   
    File simpleRuleFile = new File("test/resources/simpleRule.txt");

    RuleInterpreter ri = new RuleInterpreter();
    ri.parseRules(simpleRuleFile);

    // Now check that Rock does indeed beat scissors
    assertEquals("Rock", ri.winner("Rock","Scissors"));
    assertEquals("Rock", ri.winner("Scissors","Rock"));
  }
  
  // TODO
  // Invalid rule rejected
  // All three rules
  // Invalid rule check rejected

  
  
}
