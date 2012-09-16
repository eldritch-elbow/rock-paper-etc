package com.ebay.puzzle.rps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleInterpreter {

  private Set<String> tokens;
  private Map<Play, Result> playMap;
  
  /**
   * Initialse the RuleInterpreter with the given ruleset
   * @param ruleFile File containing the rules to parse
   * @throws FileNotFoundException If the given file cannot be found
   */
  public void parseRules(File ruleFile) throws FileNotFoundException {
    
    tokens = new HashSet<String>();
    playMap = new HashMap<Play, Result>();

    // Use BufferedReader and FileReader to read lines from the file
    FileReader fileReader = new FileReader(ruleFile);
    BufferedReader ruleReader = new BufferedReader(fileReader);
    
    try {
      
      String ruleString;      
      while ((ruleString = ruleReader.readLine()) != null) {       
        parseRule(ruleString);
      }
      
    } catch (IOException e) {
      
      // Escalate IOException to RTE; no point forcing client to deal with this
      throw new RuntimeException("IO Exception detected reading rules",e);
      
    } finally {

      // Close the rule reader, and treat IOExceptions as terminal. 
      // Could consider graceful failure but not much point in this case.
      try {
        ruleReader.close();
      } catch (IOException e) {
        throw new RuntimeException("IO Exception closing rule file",e);
      }
      
    }

  }

  /*
   * Private helper method for parsing rules
   */
  private void parseRule(String ruleString) {
    
    // Parse the rule
    String[] readTokens = ruleString.split(":");
 
    if (readTokens.length != 3) {
      throw new RuntimeException("Malformed rule detected");
    }
    
    String beater = readTokens[0];
    String method = readTokens[1];
    String beaten = readTokens[2];
        
    // Add the tokens to the known set
    tokens.add(beater);
    tokens.add(beaten);
    
    // Add the rule to the lookup mechanism
    Result r = new Result(beater, method, beaten);
    Play p = new Play(beater, beaten);

    playMap.put(p, r);
  }

  /**
   * Read-only accessor for the set of valid tokens. No order can
   * be assumed for the Set.
   * @return Set of String
   */
  public Set<String> getTokens() {    
    return Collections.unmodifiableSet( tokens );
  }

  /**
   * Returns the winner form the two provided tokens.
   * @param t1 First token in play
   * @param t2 Second token in play
   * @return The value of t1, or t2, whichever is triumphant
   * @throws InvalidStateException
   */
  public Result winner(String t1, String t2) {

    String invalidTokenMsg = "Invalid game Token '%s'";
    if (!tokens.contains(t1)) {
      throw new IllegalArgumentException( String.format(invalidTokenMsg, t1) );
    }
    if (!tokens.contains(t2)) {
      throw new IllegalArgumentException( String.format(invalidTokenMsg, t2) );      
    }
    
    // Check for a draw
    if (t1.equals(t2)) {
      return new Result(t1, Result.DRAW_VERB, t2);
    }
    
    // Need to check, at most, two map elements here. Does X beat Y, or
    // does Y beat X? If neither, throw an exception
    
    Play thisPlay = new Play(t1,t2);    
    Result res = playMap.get(thisPlay);

    return res;
  }

}
