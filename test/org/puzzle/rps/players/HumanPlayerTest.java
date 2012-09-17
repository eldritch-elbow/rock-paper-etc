package org.puzzle.rps.players;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.puzzle.rps.players.HumanConsolePlayer;
import org.puzzle.rps.players.Player;



public class HumanPlayerTest {

  final static String LINE_SEP = System.getProperty("line.separator"); 

  // Test data, streams etc
  List<String> validTokens;
  OutputStream outStream;
  
  // Object under test: assign to the interface to test in correct behaviour
  Player hp;
  
  
  @Before
  public void prep() {
    
    validTokens = new ArrayList<String>();
    validTokens.add("Rock");
    validTokens.add("Banana");
    validTokens.add("Tardis");
        
    outStream = new ByteArrayOutputStream();
  }
  
  /*
   * The user's move should be retrieved from the input stream
   */
  @Test
  public void moveBasedOnInputStream() {    
    
    InputStream inStream = new ByteArrayInputStream("2\n".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, System.out);   
    
    assertEquals("Banana", hp.getMove());    
  }

  /*
   * The human player can make multiple moves 
   */
  @Test
  public void multipleMoves() {    
    
    InputStream inStream = new ByteArrayInputStream("2\n1\n3\n2\n".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    
    assertEquals("Banana", hp.getMove());    
    assertEquals("Rock", hp.getMove());    
    assertEquals("Tardis", hp.getMove());    
    assertEquals("Banana", hp.getMove());    
  }  
  
  /*
   * Invalid input strings are ignored  
   */
  @Test
  public void invalidMoveIgnored() {    
    
    InputStream inStream = new ByteArrayInputStream("2\n3\nQ\n1\nHello world\n1\nN\n".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    
    assertEquals("Banana", hp.getMove());    
    assertEquals("Tardis", hp.getMove());    
    assertEquals("Rock", hp.getMove());    
    assertEquals("Rock", hp.getMove());    
  }
  
  /*
   * Choices that are out of rane, i.e. for non-existent tokens, are ignored
   */
  @Test
  public void outOfBoundsIgnored() {    
    
    InputStream inStream = new ByteArrayInputStream("1\n9\n1\n0\n3\n".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    
    assertEquals("Rock", hp.getMove());    
    assertEquals("Rock", hp.getMove());    
    assertEquals("Tardis", hp.getMove());    
  }
  
  /*
   * The user is prompted, on the stream, for their move
   */
  @Test
  public void userMoveRequested() {

    InputStream inStream = new ByteArrayInputStream("2\n".getBytes());
    
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    hp.getMove();    

    assertEquals("Select your token Wiggles [1-3]: ", outStream.toString());    
  }

  /* 
   * Different token counts mean different prompts
   */
  @Test
  public void promptDependsOnTokens() {

    InputStream inStream = new ByteArrayInputStream("5\n".getBytes());
    
    validTokens.add("Toaster");
    validTokens.add("Lampshade");

    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    hp.getMove();    

    assertEquals("Select your token Wiggles [1-5]: ", outStream.toString());    
  }
  
  /*
   * Test invalid inputs cause suitable prompting 
   */
  @Test
  public void invalidChoicePromptsMessage() {    
    
    InputStream inStream = new ByteArrayInputStream("4\nJ\n1".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   
    
    assertEquals("Rock", hp.getMove());
    
    String expectedOutput = 
        "Select your token Wiggles [1-3]: "+
        "Invalid choice '4', please try again!" + LINE_SEP +
        "Select your token Wiggles [1-3]: "+
        "Invalid choice 'J', please try again!" + LINE_SEP +
        "Select your token Wiggles [1-3]: ";
        
    assertEquals(expectedOutput, outStream.toString());    

  }

  /*
   * Test implementation of toString() 
   */
  @Test
  public void fromNowOnYoullBeKnownAs() {
    
    InputStream inStream = new ByteArrayInputStream("4\nJ\n1".getBytes());
    hp = new HumanConsolePlayer(validTokens, inStream, outStream);   

    assertEquals("Wiggles", hp.toString());
    
  }
  
}
