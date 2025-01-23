/*
 * RobotChenilles.java

 */

package donnees.robots;

import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;

/*Cette classe représente un robot à chenilles. Elle hérite donc de mla classe mère Robot */
public class RobotChenilles extends Robot {
    /**
     * Constructeur du robot à chenilles
     * @param position La case sur laquelle se trouve le robot
     */
    public RobotChenilles(Case position) {
        this(position, 60); // La vitesse par défaut d'un robot à chenilles est de 60km/h
    }

    /**
     * Constructeur du robot à chenilles
     * 
     * @param position La case sur laquelle se trouve le robot
     * @param vitesse  La vitesse de déplacement du robot en km/h
     */
    public RobotChenilles(Case position, double vitesse) {
        super(TypeRobot.CHENILLES, 80, 5 * 60, 8, 2000, 100, "./assets/robots/robotChenilles.png");
        this.setPosition(position);
        this.setVitesse(vitesse);
        this.remplirReservoir();
    }

    /**
     * Renvoie la vitesse de déplacement du robot selon le terrain sur lequel il fait ce déplacement
    @return La vitesse selon le type de terrain
     */
    @Override
    public double getVitesseDeplacement(NatureTerrain terrain){
        // Sa vitesse est diminué de 50% lorsqu'il se déplace en forêt
        if(terrain == NatureTerrain.FORET){
            return this.getVitesse()*0.5;
        }else if(terrain == NatureTerrain.EAU || terrain == NatureTerrain.ROCHE){ /*Il ne peut pas se déplacer sur du rocher ou de l'eau */
            return 0;
        }else {
            return this.getVitesse();
        }
    }
}
