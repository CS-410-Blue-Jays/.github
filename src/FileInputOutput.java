
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
            return fileName;
        } catch (IOException e) {
            System.out.println("\nError writing to file: " + e.getMessage());
    }
        throw new IllegalStateException("FALL THROUGH!");
    }

    /**
     * Takes a file and ouputs atoms parsed from it
     * @param fileName - name of input file
     * @return atoms list
     */
    public ArrayList<Atom> atomInput(String fileName)
    {
        ArrayList<Atom> atoms = new ArrayList<>();
        // Parse the atoms from the file
        try (FileInputStream fis = new FileInputStream(fileName + "-output.atoms")) {
                System.out.println("Reading atoms in from file " + fileName + "-output.atoms"); //log
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


    /**
     * Prints code generated atoms to given file
     * @param fileName destination file
     * @return 1 : success, 0 :fail
     */
    public String codeGenTxtOutput(String fileName, ArrayList<Code> codeList)
    {
        int loc = 0;

        try (FileOutputStream fos = new FileOutputStream(fileName + "-output.txt")) {
                fos.write("Loc\tContents\t\tOP\n".getBytes()); // Write header to file

                String output;
                for (Code code : codeList) {
                    if (!code.checkOperation().equals("HLT"))
                    {   output = loc++ + "\t" + code.toString() + "\t\t" + code.checkOperation() + "\n";
                        fos.write(output.getBytes()); //write new output
                    }
                    else
                    {   output = loc++ + "\t" + code.toString() + "\t\t" + code.checkOperation() + "\n"; // last update before ending loop (HLT)
                        fos.write(output.getBytes()); //write last output before loop exits
                        break;  // end generation after HLT
                    }
                }
                System.out.println("\nLegible results have been written to '" + fileName + "-output.txt'");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                return null;
            }

            return fileName + "-output.txt";
    }

    /**
     * Outputs generated code to .bin file 
     * @param fileName destination file
     * @return 1:sucess, 0:fail
     */
    public String codeGenBinOutput(String fileName, ArrayList<Code> codeList)
    {
            
        try(FileOutputStream file2 = new FileOutputStream(fileName + "-output.bin")) {
            for (Code code : codeList) {
                String binaryString = code.toBinaryString();

                //remove spaces in binary string
                binaryString = binaryString.replaceAll("\\s+", ""); 

                //convert binary string into bytes
                int length = binaryString.length();
                for(int i = 0; i < length; i+= 8){
                    //extract 8 bits ( 1 byte ) per loop
                    String byteString = binaryString.substring(i, Math.min(i + 8, length));

                    //convert binary string to byte and write to .bin file
                    byte b = (byte) Integer.parseInt(byteString, 2);
                    file2.write(b);
                }
            }
            System.out.println("\nResults have been written to '" + fileName + "-output.bin' Hex editor needed to view content");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return null;
        }

        return fileName + "-output.bin";
    }


    
}

