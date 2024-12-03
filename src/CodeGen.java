import java.util.ArrayList;
import java.util.HashMap;

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
    static int register = 0; // Register Starting at 0
    static HashMap<String, String> variableRegisterMap = new HashMap<>();

    public static ArrayList<Code> generate(ArrayList<Atom> insertedAtoms) {
        atoms = insertedAtoms;
        parseCode();
        return code;
    }
    public static String getOrAssignRegister(String variable) {
        if (variableRegisterMap.containsKey(variable)) {
            return variableRegisterMap.get(variable);
        } else {
            String newRegister = String.format("%05d", register++);
            variableRegisterMap.put(variable, newRegister);
            return newRegister;
        }
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
    public static int allocateRegister(int operand){
        return register++;
    }
    public static Atom getCurrentAtom(){
        return atoms.get(currentIndex);
    }

    public static void advance(){
        currentIndex++;
    }

    public static void parseADD(Atom current){

        System.out.println("ADD detected");
        // Do things here

    }

    public static void parseSUB(Atom current) {
        System.out.println("SUB detected");
        String leftRegister = getOrAssignRegister(current.checkLeft());
        String resultRegister = getOrAssignRegister(current.checkResult());
        int operation = Code.Operation.SUB.ordinal();
        Code newInstruction = new Code(operation, 0, Integer.parseInt(resultRegister), Integer.parseInt(leftRegister));
        code.add(newInstruction);
    }

    public static void parseMUL(Atom current){ // ~ Steven

        System.out.println("MUL detected"); // Testing Purposes
        int operation = Code.Operation.MUL.ordinal();
        int mode = 0;
        int r = Integer.parseInt(current.checkResult());
        int a = Integer.parseInt(current.checkLeft());
        Code newInstruction = new Code(operation, mode, r, a);
        code.add(newInstruction);
        

    }

    public static void parseDIV(Atom current){

        System.out.println("DIV detected");
        // Do things here

        String leftRegister = getOrAssignRegister(current.checkLeft());
        String resultRegister = getOrAssignRegister(current.checkResult());
        int operation = Code.Operation.DIV.ordinal();
        int comparison = 0;
        Code newInstruction = new Code(operation, 0, Integer.parseInt(resultRegister), Integer.parseInt(leftRegister));
        code.add(newInstruction);

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
