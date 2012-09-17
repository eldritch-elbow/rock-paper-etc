package org.puzzle.rps.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileSupport {

  private static String LINE_SEP = System.getProperty("line.separator");
  
  /**
   * 
   *   http://stackoverflow.com/questions/4716503/best-way-to-read-a-text-file
   * 
   * @param resource
   * @return
   * @throws FileNotFoundException 
   */
  public static String getStringFromFile(String file) throws FileNotFoundException {

    BufferedReader br = new BufferedReader(new FileReader(file));

    try {
      
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(LINE_SEP);
        line = br.readLine();
      }
      
      return sb.toString();

    } catch (IOException ioe) {
      
      /* For any sort of IO Exception, generate an RTE. Could consider trying to  
       * recover but this will ensure problem is visible */
      throw new RuntimeException("Received IO Exception when reading resource...");
      
    } finally {
      
      // Close down the reader resource; this code is exception intolerant
      try {
        br.close();
      } catch (IOException e) {
        throw new RuntimeException("Received IO Exception when shutting down reader...");
      }
      
    }

  }

}
