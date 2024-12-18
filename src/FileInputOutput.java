
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileInputOutput {

    /**
     * Takes an array of atoms and puts them in a file
     * @param atoms - arraylist to input
     * @param fileName - name of output file
     * @return filename + .atom
     */
    public String atomOutput(ArrayList<Atom> atoms, String fileName)
    {                
        String output = "";
        try (FileOutputStream fos = new FileOutputStream(fileName + "-output.atoms")) {
            for (Atom atom : atoms) {
                output += atom.toString() + "\n";
                fos.write(output.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("\nResults have been written to '" + fileName + "-atomOutput.txt'");
            return output;
        } catch (IOException e) {
            System.out.println("\nError writing to file: " + e.getMessage());
    }
    throw new IllegalStateException("FALL THROUGH!");
    }


    public ArrayList<Atom> atomInput(String filename)
    {
        ArrayList<Atom> atoms = new ArrayList<>();
        // Parse the atoms from the file
        try (FileInputStream fis = new FileInputStream(filename + "-output.atoms")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = reader.readLine()) != null) {
                    Atom atom = Atom.parseString(line);
                    atoms.add(atom);
                } 
            return atoms;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
      return null;
    }


    public idkyet codegenoutput()
    {
        
    }

}

