import java.util.ArrayList;
import java.util.HashMap;
public class CodeGen {

    // Local test for code generation
    public static void main(String[] args) {
        ArrayList<Atom> testAtoms = new ArrayList<>();
        
        testAtoms.add(new Atom(Atom.Operation.MOV, "4", "g"));
        testAtoms.add(new Atom(Atom.Operation.LBL, "LBL0"));
        testAtoms.add(new Atom(Atom.Operation.ADD, "4", "2", "h"));
        testAtoms.add(new Atom(Atom.Operation.MUL, "4", "4", "j"));
        testAtoms.add(new Atom(Atom.Operation.DIV, "30", "5", "k"));
        testAtoms.add(new Atom(Atom.Operation.SUB, "4", "2", "l"));
        CodeGen.generate(testAtoms);
    }

    static int currentAtom = 0; // Current place in atoms
    static int programCounter = 0; //Program Counter
    static ArrayList<Code> code = new ArrayList<>(); // Return this
    static ArrayList<Atom> atoms = new ArrayList<>(); // Input
    static ArrayList<String> vars = new ArrayList<>(); // Register numbers with variable names
    static HashMap<String, Integer> label_table = new HashMap<>(); // Table of all labels
    static HashMap<String, Integer> variable_table = new HashMap<>(); // Table of all variables

    public static ArrayList<Code> generate(ArrayList<Atom> insertedAtoms) {
        atoms = insertedAtoms;

        code.add(new Code(Code.Operation.CLR.ordinal(), 1)); // Point to starting address
        parseCode();

        if(label_table.isEmpty()) {
            System.out.println("No labels found");
        } else {
            System.out.println("\nLABEL TABLE\n");
            System.out.println("LBL\tLocation");
            for(String label : label_table.keySet())
                System.out.println(label + "\t" + label_table.get(label));
        }
        
        if(variable_table.isEmpty()) {
            System.out.println("No variables found");
        } else {
            System.out.println("\nVARIABLE TABLE\n");
            System.out.println("VAR\tLocation");
            for(String var : variable_table.keySet())
                System.out.println(var + "\t" + variable_table.get(var));
        }
        return code;
    }

    public static void parseCode(){
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
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.ADD.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseSUB(Atom current) { // ~ Steven
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.SUB.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseMUL(Atom current){ // ~ Steven
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.MUL.ordinal(), reg, data);
        code.add(newInstruction);        
    }

    public static void parseDIV(Atom current){ // ~ Tucker
        int data = parseReg(current.checkRight());
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.DIV.ordinal(), reg, data);
        code.add(newInstruction);
    }

    public static void parseJMP(Atom current){
        int data = label_table.get(current.checkDestination()); // Destination
        Code newInstruction = new Code(Code.Operation.JMP.ordinal(), data); // Make the instruction
        code.add(newInstruction);
    }

    public static void parseLBL(Atom current){
        label_table.put(current.checkDestination(), atoms.indexOf(current)); // Add the label to the table
    }

    public static void parseTST(Atom current){
        int data = parseReg(current.checkRight());
        int cmp = current.checkComparatorNum();
        int reg = parseReg(current.checkLeft());
        Code newInstruction = new Code(Code.Operation.CMP.ordinal(), cmp, reg, data);
        code.add(new Code(Code.Operation.LOD.ordinal(), reg, parseReg(current.checkLeft())));
        code.add(newInstruction);
    }

    public static void parseMOV(Atom current){ // ~ Brandon
        int data = parseReg(current.checkLeft());
        int reg = parseReg(current.checkResult());
        Code newInstruction = new Code(Code.Operation.STO.ordinal(), reg, data);
        code.add(newInstruction);
    }

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
            variable_table.put(reg, programCounter++);
            return vars.indexOf(reg);
        } else {
            // If not, check if there are any available registers
            throw new RuntimeException("No available registers");
        }
    }
}
