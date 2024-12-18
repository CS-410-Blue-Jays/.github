import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Backend {

    private static String fileName;

    //Constuctors
    public Backend(){};
    public Backend(String fileName){this.fileName = fileName;}

    //getter and setter
    public String getFileName(){return fileName;}
    public void setFileName(String fileName){this.fileName = fileName; }

    public static void main(String[] Args)
    {
        ArrayList<Atom> atoms = new ArrayList<>(); // create atom list
        FileInputOutput fio = new FileInputOutput();    //allows access to file input and output methods

        System.out.println("\nReading resulting atoms from file...");	// Read the atom file

        
        atoms = fio.atomInput(fileName);	//file input to atoms

        ArrayList<Code> codeList = CodeGen.generate(atoms);	// Read the file and atomize it

        System.out.println("\nGenerating Mini Architecture code...");

        String output_txt_fileName = fio.codeGenTxtOutput(fileName, codeList);	//codeGen output to .txt
        
        String output_bin_fileName = fio.codeGenBinOutput(fileName, codeList);	//codeGen output to .bin
        
        // Attempt to execute the MiniVM
        System.out.println("\nExecuting MiniVM...");
        try {
            MiniVM vm = new MiniVM(output_bin_fileName);
            vm.execute(true, false);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error executing MiniVM: " + e.getMessage());
        }

        System.exit(1);

    }
}
