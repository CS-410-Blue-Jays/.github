import java.util.*;

class GlobalOptimization {
    
    public static List<Atom> optimizeAtoms(List<Atom> atoms){
        
        ArrayList<Atom> optimizedAtoms = new ArrayList<Atom>();

        for (Atom atom : optimizedAtoms)
            {
                optimize(atom);
                optimizedAtoms.add(atom);
            }

            return optimizedAtoms;

    }

    public static Atom optimize(Atom atom){

        // fill in constructor
        Atom optimizedAtom = new Atom();
        // fill in each helper method here


        return optimizedAtom;

    }

    public static Atom optimizeMultiplication(Atom atom)
    {}


    
}