import java.util.ArrayList;
import java.util.List;

public class LocalOptimization {

  private static ArrayList<Code> optimizedCode;
  private static Code code;

    public static ArrayList<Code> optimizeCode(ArrayList<Code> optimizedCode){
        for (Code code : optimizedCode)
            {
                optimize(code);
                if (code != null)
                optimizedCode.add(code);
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
        return null;
      }
      String operator = code.checkOperation();
      int data = code.checkData();

      // switch statement for different operations
      switch(operator){
        case "MUL":
          if(data == 1){ // Ex: x * 1 = x
            return null;
          }
          break;
        case "ADD":
          if(data == 0){ // Ex: x + 0 = x
            return null;
          }
          break;
        case "SUB":
          if(data == 0){ // Ex: x - 0 = x
            return null;
          }
          break;
        case "DIV":
          if(data == 1){ // Ex x / 1 = x
            return null;
          }
          break;
        default:
          break;
      }

      // return original code if no optimization is needed
      return code;
    }
}
