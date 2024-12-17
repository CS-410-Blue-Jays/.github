import java.util.*;

class GlobalOptimization {
    
    public static List<Atom> optimizeAtoms(List<Atom> atoms){
        
        ArrayList<Atom> optimizedAtoms = new ArrayList<Atom>();

        for (Atom atom : atoms)
            {
                optimize(atom);
                if (atom != null)
                optimizedAtoms.add(atom);
            }

            return optimizedAtoms;

    }

    public static Atom optimize(Atom atom){

        switch(atom.checkOperator()){
            case "MUL":
                return optimizeMultiplication(atom);
            case "DIV":
                return optimizeDivision(atom);
            case "ADD":
            case "SUB":
                return optimizeAddSubtract(atom);
            default:
                return null;
        }
    }

    public static Atom optimizeMultiplication(Atom atom)
    {
        int left = Integer.parseInt(atom.checkLeft());
        int right = Integer.parseInt(atom.checkRight());

           // case if 0
           if(left == 0 || right == 0){
            //store/load 0
            return new Atom(Atom.Operation.MOV, "0", atom.checkResult());
           }
           // case if 1
           else if(left == 1){
            //store/load the right value
            return new Atom(Atom.Operation.MOV, atom.checkRight(), atom.checkResult());
           }
           else if(right == 1){
            //store/load the left value
            return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
           }
           
           // check if bitwise shift applicable
           // bitwise shift
           if(isPowerOfTwo(left)){
            int numShifts = logBase2(left);
            String newLeft = Integer.toString(left << numShifts);
            return new Atom(Atom.Operation.MOV, newLeft, atom.checkResult());
           }
           else if(isPowerOfTwo(right)){
            int numShifts = logBase2(right);
            String newRight = Integer.toString(right << numShifts);
            return new Atom(Atom.Operation.MOV, newRight, atom.checkResult());
           }

           //return null if no optimizations can be performed
           return null;

    }

    public static Atom optimizeDivision(Atom atom){
        int left = Integer.parseInt(atom.checkLeft());
        int right = Integer.parseInt(atom.checkRight());

        if(left == 0){
            return new Atom(Atom.Operation.MOV, "0", atom.checkResult());
        }

        if(right == 1){
            return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
        }

        if(isPowerOfTwo(right)){
            int numShifts = logBase2(right);
            String newLeft = Integer.toString(left >> numShifts);
            return new Atom(Atom.Operation.MOV, newLeft, atom.checkResult());
        }

        //return null if no optimizations are performed
        return null;
    }

    public static Atom optimizeAddSubtract(Atom atom)
        {
            // if an operand is 0, turn into store/load

            if(atom.checkRight().equals("0")){
                //Store/load the value in the left register
                //How do we express this in atoms?
                return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
            }

            else if(atom.checkLeft().equals("0")){
                if(atom.checkOperator().equals("ADD")){
                    //Store/load the value in the right register
                    return new Atom(Atom.Operation.MOV, atom.checkRight(), atom.checkResult());
                }
                else if(atom.checkOperator().equals("SUB")){
                    //Store/load the negation of the value in the right register
                    return new Atom(Atom.Operation.NEG, atom.checkRight(), atom.checkResult());
                }
            }

            //return null if no optimizations can be performed
            return null;
            
        }
    
    public static Boolean isPowerOfTwo(int n){
        if (n <= 0) {
            return false;
        }

        return (n & (n - 1)) == 0;
    }

    public static int logBase2(int n){
        return (int)(Math.log(n)/Math.log(2));
    }
        

}