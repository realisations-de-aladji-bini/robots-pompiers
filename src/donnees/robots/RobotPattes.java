/*
 * RobotPattes.java

 */

package donnees.robots;

import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;

/**
 * Classe représentant à robot à pattes
 */
public class RobotPattes extends Robot {
    /**
     * Constructeur par défaut du robot à pattes
     * Pas besoin de remplir le réservoir ici car il a une capacité infinie
     * @param position La case initiale du robot
     */
    public RobotPattes(Case position) {
        this(position, 30); // La vitesse par défaut d'un robot à pattes est de 30km/h
    }

    /**
     * Constructeur explicite du robot à pattes
     * Pas besoin de remplir le réservoir ici car il a une capacité infinie
     * 
     * @param position La position initiale
     * @param vitesse  La vitesse de déplacement du robot en km/h
     */
    public RobotPattes(Case position, double vitesse) {
        super(TypeRobot.PATTES, 30, 0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, "./assets/robots/robotPattes.png");

        this.setPosition(position);
        this.setVitesse(vitesse);
    }

    /**
     * Retourne la vitesse de déplacement du robot à pattes sur le terrain pasé en
     * paramètre
     * 
     * @return La vitesse du robot
     */
    @Override
    public double getVitesseDeplacement(NatureTerrain terrain) {
        if (terrain == NatureTerrain.EAU) {
            // Un robot à pattes ne peut pas se déplacer sur de l'eau
            return 0;
        } else if (terrain == NatureTerrain.ROCHE) {
            return 10 / 3.6;
        } else {
            return getVitesse();
        }
    }

    /**
     * Retourne une capacité de réservoir infinie
     * 
     * @return La capacité du réservoir qui est infinie
     */
    @Override
    public int getReservoir() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setReservoir(int volume){
        super.setReservoir(0);
    }

    @Override
    public void deverserEau(int volume) {
        /*Le reservoir étant infini, on ne verse rien là-dedans */
    }
}
