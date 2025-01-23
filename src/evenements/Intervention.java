/*
 * Intervention.java

 */

package evenements;

import donnees.carte.Case;
import donnees.DonneesSimulation;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import donnees.robots.RobotState;

import java.util.logging.Logger;

import chefpompiers.ChefPompier;

public class Intervention extends Evenement {
    private final DonneesSimulation donneesSimulation;
    private final Robot robot;
    private final Case caseIntervention;

    private static final Logger LOGGER = Logger.getLogger("Intervention");

    public Intervention(double date, DonneesSimulation donneesSimulation, Robot robot, Case caseIntervention) {
        super(date);

        this.donneesSimulation = donneesSimulation;
        this.robot = robot;
        this.caseIntervention = caseIntervention;
    }

    @Override
    public void execute() {
        if (this.robot.getPosition() != this.caseIntervention) {
            throw new IllegalStateException("Le robot (" + robot
                    + ") n'est pas sur la case où il est censé intervenir (" + caseIntervention + ")");
        }

        Incendie incendie = this.donneesSimulation.getIncendieSurCase(caseIntervention);

        if (incendie == null) {
            throw new IllegalStateException("Il n'y a pas d'incendie sur la case (" + caseIntervention.getLigne()
                    + ", " + caseIntervention.getColonne() + ")");
        }

        this.robot.deverserEau();
        incendie.eteindre(this.robot.getVolumeIntervention());

        ChefPompier chefPompier = this.robot.getChefPompier();
        if (chefPompier != null) {
            // Si le réservoir est vide, on demande au chef pompier pour se remplir
            if (this.robot.getReservoir() == 0) {
                chefPompier.reservoirVide(this.robot);
            }
            // Si l'incendie est éteint, on demande au chef pompier pour de nouvelles
            // instructions
            else if (incendie.estEteint()) {
                this.robot.setState(RobotState.ATTENTE);
                chefPompier.incendieEteint(incendie);
            }
            // Sinon, on programme un nouvel évènement d'intervention
            else {
                double date = chefPompier.getSimulateur()
                        .getDateSimulationDansXSecondes(robot.getDureeIntervention());
                LOGGER.info("Le robot " + robot + " va intervenir sur l'incendie " + incendie + " à la date "
                                + date);

                Intervention intervention = new Intervention(date, donneesSimulation, robot, incendie.getPosition());
                chefPompier.getSimulateur().ajouteEvenement(intervention);
            }
        }
    }

    @Override
    public String toString() {
        return "Le robot (" + robot + ") est en intervention sur l'incendie ("
                + this.donneesSimulation.getIncendieSurCase(caseIntervention) + ") (" + caseIntervention
                + ")";
    }
}
