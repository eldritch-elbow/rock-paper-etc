package org.puzzle.rps.support;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.puzzle.rps.support.FileSupport;

public class FileSupportTest {

  
  @Test
  public void getStringFromResource() throws FileNotFoundException {
    
    String res = FileSupport.getStringFromFile("test/resources/rpsRules.txt");
    
    assertTrue(res.startsWith("Rock:crushes:"));
  }

}
