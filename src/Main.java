
public class Main {
  public static void main(String[] args){

	Frontend frontend = new Frontend();
	String atom_output_fileName = frontend.executeFrontend();
	
	Backend backend = new Backend(atom_output_fileName);
	backend.main(args);

	} // End of main
} // End of class
