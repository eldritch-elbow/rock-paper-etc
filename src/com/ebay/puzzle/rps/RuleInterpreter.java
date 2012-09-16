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
  private Map< String, Set<String> > beatMap;
  
  /**
   * Initialse the RuleInterpreter with the given ruleset
   * @param ruleFile File containing the rules to parse
   * @throws FileNotFoundException If the given file cannot be found
   */
  public void parseRules(File ruleFile) throws FileNotFoundException {
    
    tokens = new HashSet<String>();
    beatMap = new HashMap<String, Set<String>>();

    // Use BufferedReader and FileReader to read lines from the file
    FileReader fileReader = new FileReader(ruleFile);
    BufferedReader ruleReader = new BufferedReader(fileReader);
    
    try {
      
      // Parse the rule
      String ruleString = ruleReader.readLine();      
      String[] readTokens = ruleString.split(":");
      
      String beater = readTokens[0];
      String beaten = readTokens[1];
      
      // Add the tokens to the known set
      tokens.add(beater);
      tokens.add(beaten);
      
      // Add the rule to the lookup mechanism
      Set<String> beatenSet = beatMap.get(beater);
      if (beatenSet == null) {
        beatenSet = new HashSet<String>();
        beatMap.put(beater, beatenSet);
      }
      
      beatenSet.add(beaten);
      
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
  public String winner(String t1, String t2) {
    
    // Need to check, at most, two map elements here. Does X beat Y, or
    // does Y beat X? If neither, throw an exception
    
    Set<String> t1Set = beatMap.get(t1);
    Set<String> t2Set = beatMap.get(t2);
    
    if (t1Set != null && t1Set.contains(t2)) {
      return t1;
    } else if (t2Set != null && t2Set.contains(t1)) {
      return t2;
    } else {
      throw new IllegalStateException();
    }
  }

}
