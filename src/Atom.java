public class Atom {
  private final Operation operation;
  private final String left;
  private final String right;
  private final String result;
  private final int comparison;
  private final String destination;

  public enum Operation {
    ADD, SUB, MUL, DIV, JUMP, TST, LBL, MOV
  }
  
  // Constructor for add/mul/div/sub operations
  public Atom(Operation operation, String left, String right, String result){
    this.operation = operation;
    this.left = left;
    this.right = right;
    this.result = result;
    this.destination = null;
    this.comparison = -1;
  }

  // Constructor for unconditional jump operations ( JMP )
  public Atom(Operation operation, String destination){
    this.operation = operation;
    this.left = null;
    this.right = null;
    this.result = null;
    this.destination = destination;
    this.comparison = -1;
  }

  // Constructor for condition test operationns ( TST )
  public Atom(Operation operation, String left, String right, String destination, int comparison){
    this.operation = operation;
    this.left = left;
    this.right = right;
    this.result = null;
    this.destination = destination;
    this.comparison = comparison;
  }

  // Constructor for mov operations ( MOV )
  public Atom(Operation operation, String left, String result){
    this.operation = operation;
    this.left = left;
    this.right = null;
    this.result = result;
    this.destination = null;
    this.comparison = -1;
  }

  public String checkOp(){
    return this.operation.toString();
  }

  public String checkLeft(){
    return this.left;
  }

  public String checkRight(){
    return this.right;
  }

  public String checkResult(){
    return this.result;
  }

  

  public String checkComparator(){
    return switch (this.comparison) {
      case 0 -> "Always";
      case 1 -> "Equal";
      case 2 -> "Lesser";
      case 3 -> "Greater";
      case 4 -> "LesserOrEqual";
      case 5 -> "GreaterOrEqual";
      case 6 -> "NotEqual";
      default -> "None";
    };
  }

  public String checkDestination(){
    return this.destination;
  }

}
