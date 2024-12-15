import java.util.*;

class GlobalOptimization {
    
    public static List<Atom> optimizeAtoms(List<Atom> atoms){
        
        ArrayList<Atom> optimizedAtoms = new ArrayList<Atom>();

        for (Atom atom : optimizedAtoms)
            {
                optimize(atom);
                if (atom != null)
                optimizedAtoms.add(atom);
            }

            return optimizedAtoms;

    }

    public static Atom optimize(Atom atom){

        // fill in constructor
    Atom optimizedAtom;
    optimizedAtom = optimizeMultiplication(atom);

        // fill in each helper method and condition to call here


        return optimizedAtom;

    }

    public static Atom optimizeMultiplication(Atom atom)
    {
           // case if 0
           if(atom.checkLeft().equals("0") || atom.checkRight().equals("0")){
            //store/load 0
           }
           // case if 1
           else if(atom.checkLeft().equals("1")){
            //store/load the right value
           }
           else if(atom.checkRight().equals("1")){
            //store/load the left value
           }
           
           // check if bitwise shift applicable
                // bitwise shift
            
            return atom;

    }

     public static Atom optimizeAddSubtract(Atom atom)
        {
            // if an operand is 0, turn into store/load

            if(atom.checkRight().equals("0")){
                //Store/load the value in the left register
                //How do we express this in atoms?
            }

            else if(atom.checkLeft().equals("0")){
                if(atom.checkOperator().equals("ADD")){
                    //Store/load the value in the right register
                }
                else if(atom.checkOperator().equals("SUB")){
                    //Store/load the negation of the value in the right register
                }
            }

            return atom;
            
        }
        

}