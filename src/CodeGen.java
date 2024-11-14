import java.util.ArrayList;

public class CodeGen {
    public static void main(String[] args) {
        ArrayList<Atom> atoms = new ArrayList<Atom>();
        atoms.add(new Atom(Atom.Operation.MOV, "4", "g"));
        atoms.add(new Atom(Atom.Operation.LBL, "LBL0"));
        atoms.add(new Atom(Atom.Operation.TST, "g", "2", "LBL1", 2));
        atoms.add(new Atom(Atom.Operation.ADD, "4", "2", "h"));
        atoms.add(new Atom(Atom.Operation.MUL, "4", "4", "j"));
        atoms.add(new Atom(Atom.Operation.DIV, "30", "5", "k"));
        atoms.add(new Atom(Atom.Operation.SUB, "4", "2", "l"));
        genCode(atoms);
    }

    public static ArrayList<Code> genCode(ArrayList<Atom> atoms) {
        parseAtoms();
        return null;
    }

    public static void parseAtoms(){
        
            
    }
}
