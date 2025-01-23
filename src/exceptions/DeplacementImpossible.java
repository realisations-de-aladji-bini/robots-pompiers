/*
 * DeplacementImpossible.java

 */

package exceptions;

/**
 * Classe indiquant qu'un déplacement n'est pas autorisé dans une certaine direction.
 */
public class DeplacementImpossible extends Exception{
    public DeplacementImpossible(String message){
        super(message);
    }
}
