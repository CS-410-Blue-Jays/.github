
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileOutput {

    /**
     * Takes an array of atoms and puts them in a file
     * @param atoms - arraylist to input
     * @param fileName - name of output file
     * @return filename + .atom
     */
    public String atomOutput(ArrayList<Atom> atoms, String fileName)
    {
        try (FileOutputStream fos = new FileOutputStream(fileName + "-output.atoms")) {

            for (Atom atom : atoms) {
                String output = "";
                output += atom.toString() + "\n";
                fos.write(output.getBytes(StandardCharsets.UTF_8));
                return output;
            }
            System.out.println("\nResults have been written to '" + fileName + "-output.txt'");
        } catch (IOException e) {
            System.out.println("\nError writing to file: " + e.getMessage());
    }
    throw new IllegalStateException("FALL THROUGH!");
    }


}
