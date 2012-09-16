package org.puzzle.rps;

/**
 * Immutable class for game results
 */
public class Result {

  public static final String DRAW_VERB = "draws with";
  
  public final String winningToken;
  public final String winVerb;
  public final String losingToken;

  public Result(String winner, String verb, String loser) {
    this.winningToken = winner;
    this.winVerb = verb;
    this.losingToken = loser;
  }
  
  
  // AUTO-GENERATED hashCode and equals implementations ... thank you Eclipse.
  // Must always implement one if you implement the other
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((losingToken == null) ? 0 : losingToken.hashCode());
    result = prime * result + ((winVerb == null) ? 0 : winVerb.hashCode());
    result = prime * result
        + ((winningToken == null) ? 0 : winningToken.hashCode());
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
    Result other = (Result) obj;
    if (losingToken == null) {
      if (other.losingToken != null)
        return false;
    } else if (!losingToken.equals(other.losingToken))
      return false;
    if (winVerb == null) {
      if (other.winVerb != null)
        return false;
    } else if (!winVerb.equals(other.winVerb))
      return false;
    if (winningToken == null) {
      if (other.winningToken != null)
        return false;
    } else if (!winningToken.equals(other.winningToken))
      return false;
    return true;
  }


}
