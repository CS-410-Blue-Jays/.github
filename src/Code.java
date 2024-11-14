public class Code {
    private final int loc;
    private final Instruction inst;
    private final String oper1;
    private final String oper2;
    private final String oper3;

    public enum Instruction {
        lw, add, sw, jmp
    }

    @Override
    public String toString() {
        if(oper3.equals(""))
            return loc + "\t" + inst + "\t" + oper1 + ", " + oper2;
        else
            return loc + "\t" + inst + "\t" + oper1 + ", " + oper2 + ", " + oper3;
    }

    public Code(int Location, String Instruction, String Operand1, String Operand2, String Operand3){
        this.loc = Location;
        this.inst = getInstruction(Instruction);
        this.oper1 = Operand1;
        this.oper2 = Operand2;
        this.oper3 = Operand3;
    }

    public Code(int Location, String Instruction, String Operand1, String Operand2){
        this.loc = Location;
        this.inst = getInstruction(Instruction);
        this.oper1 = Operand1;
        this.oper2 = Operand2;
        this.oper3 = "";
    }

    public final Instruction getInstruction(String inst){
        return switch (inst) {
            case "lw" -> Instruction.lw;
            case "add" -> Instruction.add;
            case "sw" -> Instruction.sw;
            case "jmp" -> Instruction.jmp;
            default -> throw new IllegalArgumentException("Invalid instruction: " + inst);
        };
    }
}
