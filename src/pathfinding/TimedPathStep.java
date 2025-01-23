/*
 * TimedPathStep.java

 */

package pathfinding;

import donnees.carte.Case;
import donnees.robots.Robot;
import evenements.DeplacementCase;
import evenements.Evenement;

/**
 * Une étape d'un chemin accompagnée de sa durée
 */
public class TimedPathStep extends PathStep {
    private final double duree;

    public TimedPathStep(Case src, Case dst, double duration) {
        super(src, dst);
        this.duree = duration;
    }

    public double getDuree() {
        return duree;
    }

    /**
     * Convertit cette étape en un événement de déplacement
     * 
     * @param date
     * @param robot
     * @return
     */
    public Evenement conversionEvenement(double date, Robot robot) {
        return new DeplacementCase(date + duree, robot, to);
    }
}
