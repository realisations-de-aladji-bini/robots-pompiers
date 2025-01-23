/*
 * RobotRoues.java

 */

package donnees.robots;

import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;

/**
 * Un autre autre type de robot : Le robot à roue
 */
public class RobotRoues extends Robot {
    /**
     * Constructeur par défaut avec la vitesse de 80km/h
     * @param position La case initiale du robot
     */
    public RobotRoues(Case position) {
        this(position, 80); // La vitesse par défaut d'un robot à roues est de 80km/h
    }

    /**
     * Constructeur explicite du robot à roues
     * 
     * @param position La case initiale du robot
     * @param vitesse  La vitesse de déplacement du robot en km/h
     */
    public RobotRoues(Case position, double vitesse) {
        super(TypeRobot.ROUES, Integer.MAX_VALUE, 10 * 60, 5, 5000, 100, "./assets/robots/robotRoues.png");

        this.setPosition(position);
        this.setVitesse(vitesse);
        this.remplirReservoir();
    }

    @Override
    public double getVitesseDeplacement(NatureTerrain terrain) {
        if (terrain != NatureTerrain.HABITAT && terrain != NatureTerrain.TERRAIN_LIBRE) {
            // Un robot à roues ne peut se déplacer que sur de l'habitat ou du terrain libre
            return 0;
        }
        return this.getVitesse();
    }
}
