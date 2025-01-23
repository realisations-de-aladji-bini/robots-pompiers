/*
 * RobotState.java

 */

package donnees.robots;

// Etats possibles du robot. Les transisitions sont gérées par les évènements
public enum RobotState {
    ATTENTE,
    DEPLACEMENT_OCCUPE,
    RECHARGEMENT_OCCUPE,
    DEVERSEMENT_EAU,
    DEPLACEMENT_LIBRE,
    RECHARGEMENT_LIBRE
}
