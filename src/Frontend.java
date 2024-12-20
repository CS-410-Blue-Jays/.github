import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Frontend {
    /**
     * Runs everything from user input to atom file output
     * @return fileName of outputted atoms
     */
    public static ArrayList<Atom> executeFrontend(String defaultPath) {
        System.out.println("\nValidating file path: " + defaultPath); //src/HelloWorld.c
    
        if (defaultPath == null || defaultPath.trim().isEmpty()) {
            System.out.println("The given file path is null or empty, terminating frontend...");
            System.exit(1);
        }
    
        File newFile = new File(defaultPath);
        if (!newFile.exists() || !newFile.isFile()) {
            System.out.println("The given file path is invalid or does not point to a file, terminating frontend...");
            System.exit(1);
        }
    
        if (!newFile.canRead()) {
            System.out.println("The file cannot be read due to insufficient permissions, terminating frontend...");
            System.exit(1);
        }
    
        String fileName = newFile.getName();
        if (!fileName.contains(".")) {
            System.out.println("The file has no extension, terminating frontend...");
            System.exit(1);
        }
    
        ArrayList<Token> tokens = new ArrayList<>();
    
        try (RandomAccessFile file = new RandomAccessFile(newFile, "r")) {
            System.out.println("\nTokenizing file: '" + newFile.getName() + '\'');
    
            StringBuilder inputBuilder = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                inputBuilder.append(line).append('\n');
            }
            tokens.addAll(Scanner.scan(inputBuilder.toString()));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(-1);
        }
    
        if (tokens.isEmpty()) {
            System.err.println("No tokens found! Try pointing to a different file.");
            System.exit(1);
        }
    
        FileInputOutput fio = new FileInputOutput();
        fio.tokenOutput(tokens, fileName);
    
        System.out.println("\nParsing tokens...");
        ArrayList<Atom> atoms = Parser.parse(tokens);
        
        return atoms;
    }
    
}
