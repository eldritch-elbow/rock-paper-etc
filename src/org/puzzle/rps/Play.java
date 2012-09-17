package org.puzzle.rps;

import java.util.HashSet;
import java.util.Set;

/**
 * Immutable object used for simplicity in looking up results.
 */
public class Play {

  private final Set<String> playedTokens;
  
  public Play(String t1, String t2) {
    
    /* Store the two tokens in a Set. This allows the "play" to be
     * created with the tokens in any order but to be consistently
     * comparable.
     */
    playedTokens = new HashSet<String>();
    playedTokens.add(t1);
    playedTokens.add(t2);
    
  }

  // AUTO-GENERATED hashCode and equals implementations ... thank you Eclipse.
  // Must always implement one if you implement the other
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((playedTokens == null) ? 0 : playedTokens.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Play other = (Play) obj;
    if (playedTokens == null) {
      if (other.playedTokens != null)
        return false;
    } else if (!playedTokens.equals(other.playedTokens))
      return false;
    return true;
  }
  
}
