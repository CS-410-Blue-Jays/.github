import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
  public static void main(String[] args){

	// Testing out Jframe
	JFrame codeCompiler = new JFrame();

	// File Chooser
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src/"));
	fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files", "c"));
	int returnValue = fileChooser.showOpenDialog(codeCompiler);
	String path = "";

	// If a file is selected, get the path
	if (returnValue == JFileChooser.APPROVE_OPTION)
		path = fileChooser.getSelectedFile().getAbsolutePath();
	
	ArrayList<Token> tokens = new ArrayList<>();
	File newFile = new File(path);
	String fileName = newFile.getName().substring(0, newFile.getName().lastIndexOf('.'));

	// Read the file and tokenize it
	try (RandomAccessFile file = new RandomAccessFile(newFile.getAbsolutePath(), "r")) {
		System.out.println("Tokenizing file: '" + newFile.getName() + '\'');

		// Append each line to the total input
		String input = "";

		String line;
		while((line = file.readLine()) != null)
			input += line + '\n';
		tokens.addAll(Scanner.scan(input));
		file.close();
	} catch(IOException e){
		String error = e.getLocalizedMessage();
		System.out.println("Error reading file '"+ newFile.getName() + "': " + error.substring(error.indexOf('(') + 1, error.length()-1) + ", check the file path and try again.");
		System.exit(-1);
	}

	// Once all tokens are found, print them
	if(tokens.isEmpty()){
		System.err.println("No tokens found! Try pointing to a different file.");
		System.exit(1);
	}
	for(Token tok : tokens)
		System.out.println(tok.toString());

	// Parse the tokens
	System.out.println("\nParsing tokens...");
	ArrayList<Atom> atoms = Parser.parse(tokens);

	FileInputOutput fio = new FileInputOutput(); //init class, saves space in main.java

	String atom_output_fileName = fio.atomOutput(atoms, fileName);	// atom output to file
	
	System.out.println("\nReading resulting atoms from file...");	// Read the atom file

	atoms = new ArrayList<>(); // Flush the atoms
	
	atoms = fio.atomInput(atom_output_fileName);	//file input to atoms

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
    } // End of main
} // End of class
