import java.io.File;  // Import the File class
import java.io.FileWriter;  // Import the IOException class to handle errors
import java.io.IOException;   // Import the FileWriter class

public class FileOutput {
    public static void main(String[] args) {
      try {
        

        File myObj = new File("src/output.txt");
        if (myObj.createNewFile()) {
          System.out.println("File created: " + myObj.getName());
        } else {
          System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();

      }      
        writeToFile("TSTEINGSIDFNS ");

    }
    
  public static void writeToFile(String str) {
    try {
      FileWriter myWriter = new FileWriter("src/output.txt");
      myWriter.write(str);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }


}