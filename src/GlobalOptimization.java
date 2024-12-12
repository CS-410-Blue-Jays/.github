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
           // case if 1
           
           // check if bitwise shift applicable
                // bitwise shift
            
            return atom;

    }

     public static Atom optimizeAddSubtract(Atom atom)
        {
            // if an operand is 0, turn into store/load

            return atom;
            
        }
        

}