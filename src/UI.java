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
            int flag = 0; // 0 for off, 1 for on (optimization)
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
            String filePath = "HelloWorld.c";

            System.out.println("Would you like to execute the frontend?(Y/N)");
            String response = prompt.next().trim().toUpperCase();
            // prompt.close();
            if(!response.equals("Y"))
            {
                System.out.println("The application will close now" );
                System.exit(0);
                return null;
            }

            System.out.println("Received: " + response);

            //handle file path
            System.out.println("Would you like to set the default file path to scan and parse for HelloWorld.c?(Y/N)");
            response = prompt.next().trim().toUpperCase();
    
            if(!response.equals("Y"))
            {
                prompt.nextLine();
                //set new file path
                System.out.println("Enter new filepath for the file you would like to run the frontend applcation on: (example.txt)");
                response = prompt.nextLine().trim();
                
                //validate file path 
                File file = new File(response);
 
                // Check if the file exists
                if (file.exists()) {
                    // Check if it is a file or a directory
                    if (file.isFile()) {
                        System.out.println("The path is a valid file."); 
                        filePath = response;        //set filepath and break
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
            System.out.println("Would you like to execute the backend?(Y/N)");
            String response = prompt.next().trim().toUpperCase();
            // prompt.close();
            if(!response.equals("Y"))
            {
                System.out.println("The application will close now" );
                System.exit(0);
                return null;
            }

            System.out.println("Received: " + response);

            //handle file path
            System.out.println("Would you like to accept the default file path for the backend as: " + defaultPath + "?(Y/N)");
            response = prompt.next().trim().toUpperCase();
            if(!response.equals("Y"))
            {
                //set new file path
                System.out.println("Enter new filepath for the file you would like to run the frontend applcation on: (example.txt)");
                response = prompt.nextLine().toLowerCase();
                
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
            System.out.println("Would you like to run the reduction in strength optimizer?");

           atoms = GlobalOptimization.optimizeAtoms(atoms);   

           String atom_output_fileName = fio.atomOutput(atoms, filePath);

            System.out.println("Frontend done executing");
            return atom_output_fileName;
        }
        
        public String runBackend(String atom_output_fileName)
        {
            // Backend backend = new Backend(atom_output_fileName);
            // backend.setFileName(atom_output_fileName);	//possibly prompt user for this...
            ArrayList<Code> codeList = Backend.executeBackend(atom_output_fileName);

            System.out.println("Code has been generated, would you like to optimize the code?");

            //yes -- needs prompting for flags
            codeList = LocalOptimization.optimizeCode(codeList);

            String new_codeGen_output_file = atom_output_fileName.split("-")[0].concat("-finalCodeGen.output.txt");
            String txtfile = fio.codeGenTxtOutput(new_codeGen_output_file, codeList);	//codeGen output to .txt
        
            String fiofilename = atom_output_fileName.split("-")[0].concat("-finalCodeGen.output.bin");
            String output_bin_fileName = fio.codeGenBinOutput(fiofilename, codeList);	//codeGen output to .bin
            
            return output_bin_fileName;
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
    

