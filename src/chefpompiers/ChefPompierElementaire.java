/*
 * ChefPompierElementaire.java

 */

package chefpompiers;

import java.util.HashMap;
import java.util.HashSet;
import donnees.DonneesSimulation;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import donnees.robots.RobotState;
import evenements.DecisionChefPompier;
import pathfinding.Path;
import simulations.Simulateur;
import simulations.StrategieSimulation;

public class ChefPompierElementaire extends ChefPompier {
    private final int DELAI_ACTION = 2;
    protected HashMap<Incendie, Robot> affectations;

    public ChefPompierElementaire(DonneesSimulation donneesSimulation, Simulateur simulateur) {
        super(donneesSimulation, simulateur, StrategieSimulation.ELEMENTAIRE);

        this.affectations = new HashMap<>();
        this.prendreDecision();
    }

    @Override
    public void prendreDecision() {
        // On affecte un robot à chaque incendie non éteint
        for (Incendie incendie : this.donneesSimulation.getIncendies()) {
            if (incendie.estEteint() || this.affectations.containsKey(incendie)) {
                continue;
            }

            for (Robot robot : this.donneesSimulation.getRobots()) {
                if (!robot.estOccupe()) {
                    this.affecterRobot(robot, incendie);
                    break;
                }
            }
        }

        // Le chef pompier élémentaire prend une décision tous les n pas de temps
        if (!this.affectations.isEmpty()) {
            this.simulateur
                    .ajouteEvenement(new DecisionChefPompier(this.simulateur.getDateSimulation() + DELAI_ACTION, this));
        }
    }

    @Override
    public void affecterRobot(Robot robot, Incendie incendie) {
        // Si le robot est à moitié vide, on l'envoie se remplir, sinon on l'envoie sur
        // l'incendie
        if (robot.getReservoir() < robot.getCapaciteReservoir() / 2) {
            this.reservoirVide(robot);
            this.affectations.put(incendie, robot);

        } else {
            robot.intervenir(incendie);

            Path path = robot.getPathfinder().plusCourtChemin(robot, robot.getPosition(), incendie.getPosition());

            if (path == null) {
                return;
            }

            programmeDeplacement(robot, path);

            // On affecte le robot à l'incendie après s'être assuré qu'il pouvait s'y rendre
            this.affectations.put(incendie, robot);
        }
    }

    @Override
    public void enleverAffectationsIncendie(Incendie incendie) {
        this.affectations.get(incendie).setState(RobotState.ATTENTE);
        this.affectations.remove(incendie);
    }

    @Override
    public void enleverAffectationRobot(Robot robot) {
        for (Incendie incendie : this.affectations.keySet()) {
            if (this.affectations.get(incendie) == robot) {
                robot.setState(RobotState.ATTENTE);
                this.affectations.remove(incendie);
                break;
            }
        }
    }

    @Override
    public HashSet<Robot> getRobotsAffectesAIncendie(Incendie incendie) {
        HashSet<Robot> robots = new HashSet<>();
        robots.add(this.affectations.get(incendie));

        return robots;
    }

    @Override
    public Incendie getIncendieAffecteARobot(Robot robot) {
        for (Incendie incendie : this.affectations.keySet()) {
            if (this.affectations.get(incendie) == robot) {
                return incendie;
            }
        }

        return null;
    }

    @Override
    public void reinitialiser() {
        this.affectations.clear();
    }
}