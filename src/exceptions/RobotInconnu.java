/*
 * RobotInconnu.java

 */

package exceptions;

/**
 * Classe d'exception pour spécifier qu'un type de robot n'est pas connu de nos 4 robots existants
 */
public class RobotInconnu extends Exception{
    public RobotInconnu(String message) {
        super(message);
    }
}
