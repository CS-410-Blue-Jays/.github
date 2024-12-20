import java.util.ArrayList;

public class Backend {

    private static String fileName;

    //Constuctors
    public Backend(){};
    public Backend(String fileName){this.fileName = fileName;}

    //getter and setter
    public String getFileName(){return fileName;}
    public void setFileName(String fileName){this.fileName = fileName; }

    public static ArrayList<Code> executeBackend(String fileName)
    {
        ArrayList<Atom> atoms = new ArrayList<>(); // create atom list
        FileInputOutput fio = new FileInputOutput();    //allows access to file input and output methods

        System.out.println("\nReading resulting atoms from file...");	// Read the atom file

        
        atoms = fio.atomInput(fileName);	//file input to atoms

        ArrayList<Code> codeList = CodeGen.generate(atoms);	// Read the file and atomize it

        System.out.println("\nGenerating Mini Architecture code...");


        return codeList;
    }
}
