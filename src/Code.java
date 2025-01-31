public class Code {

    private final long code; // The full code (the mini will implement this in 32 bits)

    // Possible operations for the Mini architecture (In-order: 0-9)
    public enum Operation {
        CLR, // Clear Floating-Point Register
        ADD, // Add Floating-Point Registers
        SUB, // Subtract Floating-Point Registers
        MUL, // Multiply Floating-Point Registers
        DIV, // Divide Floating-Point Registers
        JMP, // Conditional Branch
        CMP, // Compare, Set Flag
        LOD, // Load Floating-Point Register
        STO, // Store Floating-Point Register
        HLT  // Halt Processor
    }

    // Need to return a string representation of the Code object
    @Override
    public String toString() {
        return addPadding((int) code);
    }

    public String toBinaryString(){

        String num = addPadding((int) code);
        String out = "";

        for(int i = 0; i < num.length(); i++){
            out += switch (num.charAt(i)) {
                case '1' -> "0001 ";
                case '2' -> "0010 ";
                case '3' -> "0011 ";
                case '4' -> "0100 ";
                case '5' -> "0101 ";
                case '6' -> "0110 ";
                case '7' -> "0111 ";
                case '8' -> "1000 ";
                case '9' -> "1001 ";
                case 'A' -> "1010 ";
                case 'B' -> "1011 ";
                case 'C' -> "1100 ";
                case 'D' -> "1101 ";
                case 'E' -> "1110 ";
                case 'F' -> "1111 ";
                default -> "0000";
            };
        }

        return out;
    }

    // Constructor for the CMP instruction
    public Code(int Operation, int Comparator, int Register, int Data){
        this.code = Operation * 10000000 + Comparator * 1000000 + Register * 100000 + Data;
    }

    // Constructor for the CLR, ADD, SUB, MUL, DIV, LOD, STO instructions
    public Code(int Operation, int Register, int Data){
        this.code = Operation * 10000000 + Register * 100000 + Data;
    }

    // Constructor for the JMP instruction
    public Code(int Operation, int Data){
        this.code = Operation * 10000000 + Data;
    }

    // Constructor for the HLT instruction
    public Code(int Operation){
        this.code = Operation * 10000000;
    }

    // Returns the operation from a passed Operation
    public final int getOperation(Operation op){
        return switch (op) {
            case CLR -> 0;
            case ADD -> 1;
            case SUB -> 2;
            case MUL -> 3;
            case DIV -> 4;
            case JMP -> 5;
            case CMP -> 6;
            case LOD -> 7;
            case STO -> 8;
            case HLT -> 9;
            default -> throw new IllegalArgumentException("Invalid instruction: " + op);
        };
    }

    public final String checkOperation(){
        return switch (Integer.parseInt(Long.toString(this.code)) / 10000000) {
            case 0 -> "CLR";
            case 1 -> "ADD";
            case 2 -> "SUB";
            case 3 -> "MUL";
            case 4 -> "DIV";
            case 5 -> "JMP";
            case 6 -> "CMP";
            case 7 -> "LOD";
            case 8 -> "STO";
            case 9 -> "HLT";
            default -> throw new IllegalArgumentException("Invalid instruction: " + this.code);
        };
    }

    // Add left-padding to the data until it has 5 places
    public final String addPadding(int num){
        return String.format("%08d", num);
    }
    public final int checkData() {
        return (int) (this.code % 100000);
    }

    //32 bits - 8 digits, need the 3rd digit and 9-12 bits
    public final int checkReg(){
        
        return (Integer.parseInt(Long.toString(this.code)) / 100000) % 10;
    }
}
