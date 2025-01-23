/*
 * ChefPompierEvolue.java

 */

package chefpompiers;

import java.util.HashMap;
import java.util.HashSet;
import donnees.carte.Case;
import donnees.DonneesSimulation;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import donnees.robots.RobotState;
import evenements.DecisionChefPompier;
import pathfinding.Path;
import pathfinding.PathStep;
import simulations.Simulateur;
import simulations.StrategieSimulation;
import java.util.function.Function;

public class ChefPompierEvolue extends ChefPompier {
    protected HashMap<Incendie, HashSet<Robot>> affectations;

    public ChefPompierEvolue(DonneesSimulation donneesSimulation, Simulateur simulateur) {
        super(donneesSimulation, simulateur, StrategieSimulation.EVOLUE);

        this.affectations = new HashMap<>();
        this.prendreDecision();
    }

    @Override
    public void prendreDecision() {
        for (Robot robot : this.donneesSimulation.getRobots()) {
            if (!robot.estOccupe()) {

                Incendie incendiePlusProche = null;
                double tempsPlusProche = Double.MAX_VALUE;

                // On affecte le robot à l'incendie non éteint le plus proche
                Function<Case, Boolean> filtre_feu = (c) -> donneesSimulation.getIncendieSurCase(c) != null && !donneesSimulation.getIncendieSurCase(c).estEteint();

                Path path = robot.getPathfinder().cheminAuPlusProche(robot, filtre_feu);
                
                if (path != null) {
                    affecterRobot(robot, donneesSimulation.getIncendieSurCase(path.getFin().get()));
                }
            }
        }
    }

    @Override
    public void affecterRobot(Robot robot, Incendie incendie) {
        // Si l'incendie n'a pas de robots affectés, on crée une nouvelle entrée
        if (!this.affectations.containsKey(incendie)) {
            this.affectations.put(incendie, new HashSet<>());
        }

        // Si le robot n'est pas rempli mais qu'il a une capacité suffisante pour
        // éteindre l'incendie, on l'envoie se remplir d'abord
        if (robot.getCapaciteReservoir() >= incendie.getEauNecessaire()
                && robot.getReservoir() < incendie.getEauNecessaire()) {

            this.reservoirVide(robot);
            this.affectations.get(incendie).add(robot);

        } else {
            robot.intervenir(incendie);

            Path path = robot.getPathfinder().plusCourtChemin(robot, robot.getPosition(), incendie.getPosition());

            if (path == null) {
                return;
            }

            programmeDeplacement(robot, path);

            // On affecte le robot à l'incendie après s'être assuré qu'il pouvait s'y rendre
            this.affectations.get(incendie).add(robot);
        }
    }

    @Override
    public void enleverAffectationsIncendie(Incendie incendie) {
        if (this.affectations == null || !this.affectations.containsKey(incendie)) {
            return;
        }

        for (Robot robot : this.affectations.get(incendie)) {
            // Si le robot est est parti pour se recharger, on le laisse finir
            // au lieu de le mettre en attente (prêt à être affecté à un autre incendie)
            if (robot.getState() != RobotState.RECHARGEMENT_OCCUPE) {
                robot.setState(RobotState.ATTENTE);
            }
        }

        this.affectations.remove(incendie);
        this.prendreDecision();
    }

    @Override
    public void enleverAffectationRobot(Robot robot) {
        for (Incendie incendie : this.affectations.keySet()) {
            robot.setState(RobotState.ATTENTE);
            this.affectations.get(incendie).remove(robot);
        }

        this.prendreDecision();
    }

    @Override
    public HashSet<Robot> getRobotsAffectesAIncendie(Incendie incendie) {
        return this.affectations.get(incendie);
    }

    @Override
    public Incendie getIncendieAffecteARobot(Robot robot) {
        for (Incendie incendie : this.affectations.keySet()) {
            if (this.affectations.get(incendie).contains(robot)) {
                return incendie;
            }
        }

        return null;
    }

    @Override
    public void reservoirVide(Robot robot) {
        super.reservoirVide(robot);

        double date = this.simulateur.getDateSimulationDansXSecondes(robot.getDureeRemplissage());

        // Une fois le remplissage terminé, le chef pompier doit prendre une décision
        // automatiquement
        this.simulateur.ajouteEvenement(new DecisionChefPompier(date + 1, this));
    }

    @Override
    public void reinitialiser() {
        this.affectations.clear();
    }
}