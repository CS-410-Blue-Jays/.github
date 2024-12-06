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

    static int currentAtom = 0; // Current place in atoms
    static ArrayList<Code> code = new ArrayList<>(); // Return this
    static ArrayList<Atom> atoms = new ArrayList<>(); // Input
    static ArrayList<String> vars = new ArrayList<>(); // Register numbers with variable names
    static HashMap<String, Integer> label_table = new HashMap<>(); // Table of all labels

    public static ArrayList<Code> generate(ArrayList<Atom> insertedAtoms) {
        atoms = insertedAtoms;
        parseCode();
        return code;
    }

    public static void parseCode(){
        for(Atom a : atoms){
            if(a.checkOperator().equals("LBL"))
                parseAtom();
        }
        while(hasMoreAtoms())
            parseAtom();
        code.add(new Code(Code.Operation.HLT.ordinal())); // Add the HALT instruction at the end
    }

    public static boolean hasMoreAtoms(){
        return currentAtom < atoms.size();
    }

    public static void parseAtom(){
        Atom curr = getCurrentAtom();
        switch(curr.checkOperator()){
            case "ADD" -> parseADD(curr);
            case "SUB" -> parseSUB(curr);
            case "MUL" -> parseMUL(curr);
            case "DIV" -> parseDIV(curr);
            case "JMP" -> parseJMP(curr);
            case "LBL" -> parseLBL(curr);
            case "TST" -> parseTST(curr);
            case "MOV" -> parseMOV(curr);
            default -> throw new RuntimeException("Invalid operation: " + curr.checkOperator());
        }
        advance(); // Move to the next atom
    }

    public static Atom getCurrentAtom(){
        return atoms.get(currentAtom);
    }

    public static void advance(){
        currentAtom++;
    }

    public static void parseADD(Atom current){ // ~ Brandon
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.ADD.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseSUB(Atom current) { // ~ Steven
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.SUB.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseMUL(Atom current){ // ~ Steven
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.MUL.ordinal(), reg, data);
        code.add(newInstruction);        
    }

    public static void parseDIV(Atom current){ // ~ Tucker
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.DIV.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseJMP(Atom current){
        int data = findLocation(current.checkDestination()); // Destination
        Code newInstruction = new Code(Code.Operation.JMP.ordinal(), data); // Make the instruction
        code.add(newInstruction);
    }

    public static void parseLBL(Atom current){
        label_table.put(current.checkDestination(), atoms.indexOf(current)); // Add the label to the table
    }

    public static void parseTST(Atom current){
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.CMP.ordinal(), current.checkComparatorNum(), reg, data);
        code.add(newInstruction);
    }

    public static void parseMOV(Atom current){ // ~ Brandon
        int data = parseReg(current.checkLeft());
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.LOD.ordinal(), reg, data);
        code.add(newInstruction);
    }

    // Return the register number of the variable, or the literal
    public static int parseReg(String reg){
        // First, check if it is a variable or a literal
        try {
            return Integer.parseInt(reg);
        } catch (NumberFormatException e) {}

        // Second, check if the variable name already has an associated register
        if(vars.contains(reg)){
            return vars.indexOf(reg);
        } else if (vars.size() != 16){
            vars.add(reg);
            return vars.indexOf(reg);
        } else {
            // If not, check if there are any available registers
            throw new RuntimeException("No available registers");
        }
    }

    // Find the destination of a label in the label_table
    public static int findLocation(String label){
        if(label_table.containsKey(label))
            return label_table.get(label);
        else
            throw new RuntimeException("Label not found: " + label);
    }
}
