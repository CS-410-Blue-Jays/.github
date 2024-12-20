import java.util.ArrayList;

public class LocalOptimization {

  private static ArrayList<Code> optimizedCode = new ArrayList<>();
  // private static Code code;


  //*Purpose is to taka a <Code> and check if it's purpose is irrelevant in algebra*
  //
  //i.e: x = x + 0 ,  y = y * 1
  //
  //
    public static ArrayList<Code> optimizeCode(ArrayList<Code> unOptimizedCode){
      for (Code code : unOptimizedCode)
      {
        Code newCode = optimize(code);
        if (newCode != null)
        optimizedCode.add(newCode);
        
      }

          return optimizedCode;
    }
    
    public static Code optimize(Code code){
      Code optimizedCode;
      optimizedCode = simpleOptimization(code);
      return optimizedCode;
    }
   
    public static Code simpleOptimization(Code code){
      if(code == null){
        System.out.println("Code is null");
        return null;
      }

      String operator = code.checkOperation();
    
      int data = code.checkData(); //this is suppos to extract data value

      int reg = code.checkReg();
      System.out.println("operator: " + operator);
      // switch statement for different operations
      switch(operator){
        case "MUL":
          if(data == 1){ // Ex: x * 1 = x
            System.out.println("HIT!!!!!\n\n MUL case, code.checkReg() returns: " + code.checkReg() );
            // Code newCode = new Code(Operat,  )
            return null;
          }
          break;
        case "ADD":
          System.out.println("IN \"ADD\", Data = " + data);
          if(data == 0){ // Ex: x + 0 = x
            System.out.println("HIT!!!!!\n\n ADD case, code.checkReg() returns: " + code.checkReg() );

            return null;
          }
          break;
        case "SUB":
          if(data == 0){ // Ex: x - 0 = x
            System.out.println("HIT!!!!!\n\n SUB case, code.checkReg() returns: " + code.checkReg() );

            return null;
          }
          break;
        case "DIV":
          if(data == 1){ // Ex x / 1 = x
            System.out.println("\nHIT!!!!!\n DIV case, code.checkReg() returns: " + code.checkReg() );

            return null;
          }
          break;
        default:
          System.out.println("No match found for operator: " + operator);

          break;
      }

      // return original code if no optimization is needed
      return code;
    }
}





// /** Inserted code from connor, needs integrated */
//  //@auth Connor White
//  static Code loadOptimized(Code code){
  
//   int reg = code.checkData() / 1000; // this is supposed to extract the reg for the data 
//   // need to grab regesters not sure if this is right 

//   int data = code.checkData() % 1000; //this is suppos to extract data value
//   // need to grab data not sure if this is right 

// // Checking if the a load is floolwed by a store immeadeatly to the same regester
// if(CodeGen.code.size() > CodeGen.programCounter + 1){
// Code nextInstruction = CodeGen.code.get(CodeGen.programCounter + 1);
// if(nextInstruction.checkOperation().equals("STO") && nextInstruction.checkData() / 1000 == reg){
//    // Remove the load operation if its result is not used before the store
//   return null;
// }
// }

// //checks if the loaded value is a constant an can be propagated
// if(data < 1000){
// // uses teh const value direcly instead of loading from mem
// //^ can be changed if needed 

// //the return has an error uncomment to fix this is just so i can commit this branch
// //return new Code(Code.Operation.MOV.ordinal(), reg, data);
// }
// return code; // no optimazation applyed 
// }  
// //@auth Connor White
// public static Code storeOptimized(Code code){
// int reg = code.checkData() / 1000;
// int data = code.checkData() % 1000;

// //checks if the store operation is followed by a load from the same loacation
// if(CodeGen.programCounter > 0){
// Code prevInstructions = CodeGen.code.get(CodeGen.programCounter -1);
// if(prevInstructions.checkOperation().equals("lod") && prevInstructions.checkData() % 1000 == data){
//   // remove the store operation if loaded value is not modfied
//   return null;
// }
// }
// //checks if the value is used after an arithamtic operation 
// if(CodeGen.programCounter + 1 < CodeGen.code.size()){
// Code nextInstruction = CodeGen.code.get(CodeGen.programCounter + 1);
// if(isArithmeticOperation(nextInstruction) && nextInstruction.checkData() / 1000 == reg) {
//   //use the value directly in the arithamic operation instad of store and load
//   return new Code(nextInstruction.getOperation(null), nextInstruction.checkData() / 1000, data);
// }
// }
// //no optsmation was used
// return code;
// }
// private static boolean isArithmeticOperation(Code code) {
// String operation = code.checkOperation();
// return operation.equals("ADD") || operation.equals("SUB") || operation.equals("MUL") || operation.equals("DIV");
// }