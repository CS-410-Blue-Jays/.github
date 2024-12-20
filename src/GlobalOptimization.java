import java.util.*;

class GlobalOptimization {
    
    public static ArrayList<Atom> optimizeAtoms(ArrayList<Atom> atoms){
        
        ArrayList<Atom> optimizedAtoms = new ArrayList<Atom>();

        for (Atom atom : atoms)
            {
                Atom newAtom = optimize(atom);
                if (newAtom != null){
                    optimizedAtoms.add(newAtom); 
                    System.out.println("Added optimized atom of type " +newAtom.checkOperator());
                }

                //debugging statement
                //if it doesn't add an oprtimized atom, print that
                else{
                    System.out.println("Atom not optimized");
                    //add the original atom, since no otimizations could be made
                    optimizedAtoms.add(atom);
                }
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
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Multiply by 0");

            //store/load 0
            return new Atom(Atom.Operation.MOV, "0", atom.checkResult());
           }
           // case if 1
           else if(left == 1){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Multiply by 1");

            //store/load the right value
            return new Atom(Atom.Operation.MOV, atom.checkRight(), atom.checkResult());
           }
           else if(right == 1){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Multiply by 1");

            //store/load the left value
            return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
           }
           
           // check if bitwise shift applicable
           // bitwise shift
           if(isPowerOfTwo(left)){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Bitwise shift");

            int numShifts = logBase2(left);
            String newRight = Integer.toString(right << numShifts);
            return new Atom(Atom.Operation.MOV, newRight, atom.checkResult());
           }

           else if(isPowerOfTwo(right)){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Bitwise shift");

            int numShifts = logBase2(right);
            String newLeft = Integer.toString(left << numShifts);
            return new Atom(Atom.Operation.MOV, newLeft, atom.checkResult());
           }

           //return null if no optimizations can be performed
           return null;

    }

    public static Atom optimizeDivision(Atom atom){
        int left = Integer.parseInt(atom.checkLeft());
        int right = Integer.parseInt(atom.checkRight());

        //if dividing by 0, do not optimize
        if(right == 0){
            return null;
        }

        if(left == 0){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Divide 0");

            return new Atom(Atom.Operation.MOV, "0", atom.checkResult());
        }

        if(right == 1){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Divide by 1");

            return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
        }

        if(isPowerOfTwo(right)){
            //debugging statement
            System.out.println("Optimization made to " +atom.checkOperator()+ "; Bitwise shift");

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

                //debugging statement
                System.out.println("Optimization made to " +atom.checkOperator()+ "; Add/Subtract by 0");

                return new Atom(Atom.Operation.MOV, atom.checkLeft(), atom.checkResult());
            }

            else if(atom.checkLeft().equals("0")){
                if(atom.checkOperator().equals("ADD")){
                    //debugging statement
                    System.out.println("Optimization made to " +atom.checkOperator()+ "; Add to 0");

                    //Store/load the value in the right register
                    return new Atom(Atom.Operation.MOV, atom.checkRight(), atom.checkResult());
                }
                else if(atom.checkOperator().equals("SUB")){
                    //debugging statement
                    System.out.println("Optimization made to " +atom.checkOperator()+ "; Subtract from 0");

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