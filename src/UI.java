import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class UI {
    
    static java.util.Scanner prompt = new java.util.Scanner(System.in);
    static FileInputOutput fio = new FileInputOutput();
    
    public void execute()
    {
        String startFile;
        String default_file_for_backend;
        String default_file_for_VM;
        ASCII_ART();
        startFile = startPrompt();
        default_file_for_backend = runFrontend(startFile);
        
        default_file_for_backend = preBackendPrompt(default_file_for_backend);
        default_file_for_VM = runBackend(default_file_for_backend);

        System.out.println("default_file_for_VM: " + default_file_for_VM);
        String path = validateFilePath(default_file_for_VM);
        
        runVM(path);
    }
    
    public String startPrompt()
    {
        String filePath = "src/input/HelloWorld.c";

        System.out.println("Hit enter to continue...");
        System.console().readPassword();

        // Clear the screen and flush console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        //handle file path
        System.out.println("Would you like to set the default file path to scan and parse for HelloWorld.c? (Y/N)");
        String response = prompt.next().trim().toUpperCase();
        if(!response.equals("Y"))
        {
            //set new file path
            System.out.println("Enter new filepath for the file you would like to run the frontend applcation on: (example.c)");
            response = prompt.nextLine().toLowerCase();
            
            //validate file path 
            File file = new File(response);

            // Check if the file exists
            if (file.exists()) {
                // Check if it is a file or a directory
                if (file.isFile()) {
                    System.out.println("The path is a valid file."); 
                    filePath = response;        // Set filepath and break
                } else {
                    System.out.println("The path exists but is not a file");
                    System.exit(1);
                }
            } else {
                System.out.println("The path does not exist.");
                System.exit(1);
            }
        }
        
        return filePath; 
    }


    public String preBackendPrompt(String defaultPath)
    {
        //prompt user if they want to use default path
        String filepath = defaultPath;

        //take in new path if necessary
        System.out.println("Would you like to execute the backend? (Y/N)");
        String response = prompt.next().trim().toUpperCase();
        // prompt.close();
        if(!response.equals("Y"))
        {
            System.out.println("The application will close now" );
            System.exit(0);
            return null;
        }

        //handle file path
        System.out.println("Would you like to accept the default file path \nfor the backend as: " + defaultPath + "? (Y/N)");
        response = prompt.next().trim().toUpperCase();
        if(!response.equals("Y"))
        {
            //set new file path
            System.out.println("Enter new filepath for the file you would like to run the frontend applcation on: (example.txt) ");
            response = prompt.nextLine();
            
            //validate file path 
            File file = new File(response);

            // Check if the file exists
            if (file.exists()) {
                // Check if it is a file or a directory
                if (file.isFile()) {
                    System.out.println("The path is a valid file."); 
                    filepath = response;        //set filepath and break
                } else {
                    System.out.println("The path exists but is not a file");
                    System.exit(1);
                }
            } else {
                System.out.println("The path does not exist.");
                System.exit(1);
            }

        }
        //run return validated path

        //DO NOT RUN BACKEND here
        
        return filepath;

    }
    
    
    public String runFrontend(String filePath)
    {
        ArrayList<Atom> atoms = Frontend.executeFrontend(filePath);

        //Prompt user if they want to optimize...
        System.out.println("Done.\n\nWould you like to enable Global Optimization (Reduction in Strength)? (Y/N)");
        String response = prompt.next().trim().toUpperCase();
        if(response.equals("Y"))
            atoms = GlobalOptimization.optimizeAtoms(atoms);

        String atom_output_fileName = fio.atomOutput(atoms, filePath.split("/")[2]);

        System.out.println("\nFrontend has finished.\n");
        return atom_output_fileName;
    }
    
    public String runBackend(String fileName)
    {
        // Backend backend = new Backend(atom_output_fileName);
        // backend.setFileName(atom_output_fileName);	//possibly prompt user for this...
        ArrayList<Code> code = Backend.executeBackend(fileName);

        System.out.println("Would you like to enable Local Optimization? (Y/N)");
        String response = prompt.next().trim().toUpperCase();
        if(response.equals("Y")){}
            // code = GlobalOptimization.optimizeCode(code);

        fio.codeGenTxtOutput(fileName + "-output.txt", code);	//codeGen output to .txt

        return fio.codeGenBinOutput(fileName + "-output.bin", code);	//codeGen output to .bin
    }

    public void runVM(String defaultFile)
    {
            // Attempt to execute the MiniVM
        System.out.println("\nExecuting MiniVM...");
        try {
            MiniVM vm = new MiniVM(defaultFile);
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
    
    /**
     * Runs the command line interface ACII ART for our application
     */
    public void ASCII_ART()
    {
        System.out.println("\n\n\n\n");
        System.out.println( " ▄▄▄▄    ██▓     █    ██ ▓█████     ▄▄▄██▀▀▀▄▄▄     ▓██   ██▓  ██████ \n"  +
                            " ▓█████▄ ▓██▒    ██  ▓██▒▓█   ▀       ▒██  ▒████▄    ▒██  ██▒▒██    ▒\n"+
                            " ▒██▒ ▄██▒██░    ▓██  ▒██░▒███         ░██  ▒██  ▀█▄   ▒██ ██░░ ▓██▄   \n"+
                            " ▒██░█▀  ▒██░    ▓▓█  ░██░▒▓█  ▄    ▓██▄██▓ ░██▄▄▄▄██  ░ ▐██▓░  ▒   ██▒\n"+
                            " ░▓█  ▀█▓░██████▒▒▒█████▓ ░▒████▒    ▓███▒   ▓█   ▓██▒ ░ ██▒▓░▒██████▒▒\n"+
                            " ░▒▓███▀▒░ ▒░▓  ░░▒▓▒ ▒ ▒ ░░ ▒░ ░    ▒▓▒▒░   ▒▒   ▓▒█░  ██▒▒▒ ▒ ▒▓▒ ▒ ░\n"+
                            " ▒░▒   ░ ░ ░ ▒  ░░░▒░ ░ ░  ░ ░  ░    ▒ ░▒░    ▒   ▒▒ ░▓██ ░▒░ ░ ░▒  ░ ░\n"+
                            "  ░    ░   ░ ░    ░░░ ░ ░    ░       ░ ░ ░    ░   ▒   ▒ ▒ ░░  ░  ░  ░  \n"+
                            " ░          ░  ░   ░        ░  ░    ░   ░        ░  ░░ ░           ░  \n"+
                            "      ░                                              ░ ░              \n"+
                            " ▄████▄   ▒█████   ███▄ ▄███▓ ███▄ ▄███▓ ▄▄▄       ███▄    █ ▓█████▄  \n"+
                            "▒██▀ ▀█  ▒██▒  ██▒▓██▒▀█▀ ██▒▓██▒▀█▀ ██▒▒████▄     ██ ▀█   █ ▒██▀ ██▌ \n"+
                            "▒▓█    ▄ ▒██░  ██▒▓██    ▓██░▓██    ▓██░▒██  ▀█▄  ▓██  ▀█ ██▒░██   █▌ \n"+
                            "▒▓▓▄ ▄██▒▒██   ██░▒██    ▒██ ▒██    ▒██ ░██▄▄▄▄██ ▓██▒  ▐▌██▒░▓█▄   ▌ \n"+
                            "▒ ▓███▀ ░░ ████▓▒░▒██▒   ░██▒▒██▒   ░██▒ ▓█   ▓██▒▒██░   ▓██░░▒████▓  \n"+
                            "░ ░▒ ▒  ░░ ▒░▒░▒░ ░ ▒░   ░  ░░ ▒░   ░  ░ ▒▒   ▓▒█░░ ▒░   ▒ ▒  ▒▒▓  ▒  \n"+
                            "  ░  ▒     ░ ▒ ▒░ ░  ░      ░░  ░      ░  ▒   ▒▒ ░░ ░░   ░ ▒░ ░ ▒  ▒  \n"+
                            "░        ░ ░ ░ ▒  ░      ░   ░      ░     ░   ▒      ░   ░ ░  ░ ░  ░  \n"+
                            "░ ░          ░ ░         ░          ░         ░  ░         ░    ░     \n"+
                            "░                                                             ░       \n"+
                            "                    ██▓     ██▓ ███▄    █ ▓█████                      \n"+
                            "                   ▓██▒    ▓██▒ ██ ▀█   █ ▓█   ▀                      \n"+
                            "                  ▒██░    ▒██▒▓██  ▀█ ██▒▒███                         \n"+
                            "                  ▒██░    ░██░▓██▒  ▐▌██▒▒▓█  ▄                       \n"+
                            "                   ░██████▒░██░▒██░   ▓██░░▒████▒                     \n"+
                            "                   ░ ▒░▓  ░░▓  ░ ▒░   ▒ ▒ ░░ ▒░ ░                     \n"+
                            "                   ░ ░ ▒  ░ ▒ ░░ ░░   ░ ▒░ ░ ░  ░                     \n"+
                            "                                                                         "
        );
        System.out.println("""



                            Welcome to the CS-410 Blue Jays Compiler.
                            
                            You will be asked to run a frontend, backend, and mini Virtual Machine.
                            
                            Before each run, you will be asked to input the name of a file you would like to test.
                            
                            Please refer to the default file, your file directory, or previous file named in the command line for this.
                            
                            For all other inquiries, please answer in \"Y\" for YES and \"N\" for NO
                            
                            Thank You



                            """ );

    }
    private String validateFilePath(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Won't run VM, given file path invalid: " + path);
        }
        return path;
    }
}
    

