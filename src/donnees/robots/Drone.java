/*
 * Drone.java

 */

package donnees.robots;

import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;

/**
 * Un type de robot ayant une réaction plus rapide que les autres en termes d'intervention.
 */
public class Drone extends Robot {
    /**
     * Constructeur avec la vitesse par défaut de 100km/h
     * @param position
     */
    public Drone(Case position) {
        this(position, 100); // La vitesse par défaut d'un drone est de 100km/h
    }

    /**
     * Constructeur avec la vitesse indiquée d'un drone.
     * 
     * @param position La case sur laquelle se trouve le drone
     * @param vitesse  La vitesse de déplacement du robot en km/h
     */
    public Drone(Case position, double vitesse) {
        super(TypeRobot.DRONE, 150, 30 * 60, 30, 10000, 10000, "./assets/robots/drone.png");

        this.setPosition(position);
        this.setVitesse(vitesse);
        this.remplirReservoir();
    }

    // Rédefinition des méthodes héritées

    /**
     * Retourne la vitesse de déplacement du drone selon le terrain sur lequel il se trouve
     * @return La vitesse de déplacement du drone
     */
    @Override
    public double getVitesseDeplacement(NatureTerrain terrain) {
        return this.getVitesse();
    }
}