import java.util.ArrayList;

public class CodeGen {

    // Local test for code generation
    public static void main(String[] args) {
        ArrayList<Atom> testAtoms = new ArrayList<>();
        
        testAtoms.add(new Atom(Atom.Operation.MOV, "4", "g"));
        testAtoms.add(new Atom(Atom.Operation.LBL, "LBL0"));
        testAtoms.add(new Atom(Atom.Operation.TST, "g", "2", "LBL1", 2));
        testAtoms.add(new Atom(Atom.Operation.ADD, "4", "2", "h"));
        testAtoms.add(new Atom(Atom.Operation.MUL, "4", "4", "j"));
        testAtoms.add(new Atom(Atom.Operation.DIV, "30", "5", "k"));
        testAtoms.add(new Atom(Atom.Operation.SUB, "4", "2", "l"));
        
        CodeGen.generate(testAtoms);
    }

    static int currentIndex = 0; // Current place in atoms
    static ArrayList<Code> code = new ArrayList<>(); // Return this
    static ArrayList<Atom> atoms = new ArrayList<>(); // Input

    public static ArrayList<Code> generate(ArrayList<Atom> insertedAtoms) {
        atoms = insertedAtoms;
        parseCode();
        return code;
    }

    public static void parseCode(){
        while(hasMoreAtoms())
            parseAtom();
    }

    public static boolean hasMoreAtoms(){
        return currentIndex < atoms.size();
    }

    public static void parseAtom(){
        Atom curr = getCurrentAtom();
        switch(curr.checkOperator()){
            case "ADD" -> parseADD(curr);
            case "SUB" -> parseSUB(curr);
            case "MUL" -> parseMUL(curr);
            case "DIV" -> parseDIV(curr);
            case "JMP" -> parseJMP(curr);
            case "NEG" -> parseNEG(curr);
            case "LBL" -> parseLBL(curr);
            case "TST" -> parseTST(curr);
            case "MOV" -> parseMOV(curr);
            default -> throw new RuntimeException("Invalid operation: " + curr.checkOperator());
        }
        advance(); // Move to the next atom
    }

    public static Atom getCurrentAtom(){
        return atoms.get(currentIndex);
    }

    public static void advance(){
        currentIndex++;
    }

    public static void parseADD(Atom current){

        // Do things here

    }

    public static void parseSUB(Atom current){

        System.out.println("SUB detected");
        // Do things here

    }

    public static void parseMUL(Atom current){

        System.out.println("MUL detected");
        // Do things here

    }

    public static void parseDIV(Atom current){

        System.out.println("DIV detected");
        // Do things here

    }

    public static void parseJMP(Atom current){

        System.out.println("JMP detected");
        // Do things here

    }

    public static void parseNEG(Atom current){

        System.out.println("NEG detected");
        // Do things here

    }

    public static void parseLBL(Atom current){

        System.out.println("LBL detected");
        // Do things here

    }

    public static void parseTST(Atom current){

        System.out.println("TST detected");
        // Do things here

    }

    public static void parseMOV(Atom current){

        System.out.println("MOV detected");
        // Do things here

    }
}
