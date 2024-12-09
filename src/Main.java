import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
public class Main {
  public static void main(String[] args){
    System.out.println("Enter the name of the file you'd like to tokenize: ");
		String path = System.console().readLine();

		if(path.equals(""))
			path = "HelloWorld.txt"; // For testing purposes
		
		ArrayList<Token> tokens = new ArrayList<>();
		File newFile = new File(path);

		if (!newFile.exists())
			newFile = new File("src/" + path); // Automatically appends src if not in the right location

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
		if(tokens.isEmpty())
			System.err.println("No tokens found! Try pointing to a different file.");
		else {
			for(Token tok : tokens)
				System.out.println(tok.toString());
		
		// Parse the tokens
		System.out.println("\nParsing tokens...");
		ArrayList<Atom> atoms = Parser.parse(tokens);
		for(Atom atom : atoms)
			System.out.println(atom.toString());

		// Generate Mini code
		System.out.println("\nGenerating Mini Architecture code...");
		CodeGen.generate(atoms);
		int loc = 0;
		System.out.println("Would you like the results to be human-legible? (y/n)");
		String legible = System.console().readLine();

		while (!legible.equals("y") && !legible.equals("n")) {
				System.out.println("Please enter 'y' or 'n'");
				legible = System.console().readLine();
		}

		try (FileOutputStream fos = new FileOutputStream("output.txt")) {
				if (legible.equals("y")) {
						System.out.println("Loc\tContents\t\tOP");
						fos.write("Loc\tContents\t\tOP\n".getBytes()); // Write header to file

						for (Code code : CodeGen.code) {
								String output;
								if (!code.checkOperation().equals("HLT")) {
										output = loc++ + "\t" + code.toString() + "\t\t" + code.checkOperation() + "\n";
								} else {
										output = loc++ + "\t" + code.toString() + "\tStop\t" + code.checkOperation() + "\n";
								}
								System.out.print(output); // Write to console
								fos.write(output.getBytes(StandardCharsets.UTF_8));
						}
						System.out.println("Results have been written to 'output.txt'");
				} else {
					try(FileOutputStream file2 = new FileOutputStream("output.bin")){
						for (Code code : CodeGen.code) {
								String output = code.toBinaryString() + "\n";
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
								System.out.print(output); // Write to console
						}
				}
				System.out.println("Results have been written to 'output.bin. Hex editor needed to view content");
			}
		} catch (IOException e) {
				System.out.println("Error writing to file: " + e.getMessage());
		}

		}
    }
}
