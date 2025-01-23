/*
 * ChefPompier.java

 */

package chefpompiers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Function;
import java.util.logging.Logger;
import donnees.DonneesSimulation;
import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import donnees.robots.RobotState;
import evenements.DeplacementCase;
import evenements.Evenement;
import evenements.Intervention;
import evenements.Remplissage;
import pathfinding.Path;
import pathfinding.PathStep;
import simulations.Simulateur;
import simulations.StrategieSimulation;

public abstract class ChefPompier {
    protected StrategieSimulation strategie;
    protected DonneesSimulation donneesSimulation;
    protected Simulateur simulateur;
    private static final Logger LOGGER = Logger.getLogger("ChefPompier");

    public ChefPompier(DonneesSimulation donneesSimulation, Simulateur simulateur, StrategieSimulation strategie) {
        this.strategie = strategie;
        this.donneesSimulation = donneesSimulation;
        this.simulateur = simulateur;

        // On attribue le chef pompier à chaque robot pour qu'ils puissent communiquer
        // avec celui-ci
        for (Robot robot : this.donneesSimulation.getRobots()) {
            robot.setChefPompier(this);
        }
    }

    /**
     * Renvoie le simulateur
     * 
     * @return Le simulateur
     */
    public Simulateur getSimulateur() {
        return this.simulateur;
    }

    /**
     * Prend une décision sur ce que chaque robot doit faire
     */
    public abstract void prendreDecision();

    /**
     * Affecte le robot à l'incendie
     * 
     * @param robot    Le robot à affecter
     * @param incendie L'incendie ciblé
     */
    public abstract void affecterRobot(Robot robot, Incendie incendie);

    /**
     * Enlève l'affectation de tous les robots à l'incendie donné
     *
     * @param incendie
     */
    public abstract void enleverAffectationsIncendie(Incendie incendie);

    /**
     * Enlève l'affectation du robot à l'incendie
     *
     * @param robot
     */
    public abstract void enleverAffectationRobot(Robot robot);

    /**
     * Renvoie la liste des robots affectés à l'incendie donné
     *
     * @param incendie
     * @return
     */
    public abstract HashSet<Robot> getRobotsAffectesAIncendie(Incendie incendie);

    /**
     * Renvoie l'incendie affecté au robot donné
     *
     * @param robot
     * @return
     */
    public abstract Incendie getIncendieAffecteARobot(Robot robot);

    /**
     * Renvoie la stratégie utilisée par le chef pompier
     * 
     * @return La stratégie utilisée par le chef pompier
     */
    public StrategieSimulation getStrategie() {
        return this.strategie;
    }

    /**
     * Programme le déplacement du robot sur le chemin donné
     * 
     * @param robot Le robot à programmer
     * @param path  Le chemin à confier à ce robot
     */
    public void programmeDeplacement(Robot robot, Path path) {
        if (path == null) {
            LOGGER.info("Le robot " + robot + " n'a pas trouvé de chemin");
            robot.setState(RobotState.ATTENTE);
            return;
        }

        double date = this.simulateur.getDateSimulation();

        for (PathStep step : path.getSteps()) {
            // Temps de déplacement renvoie le temps en secondes pour aller d'une case à
            // l'autre
            double temps = robot.tempsDeDeplacement(step.from, step.to, this.donneesSimulation.getCarte());

            // On programme l'évènement de déplacement à la date actuelle + la durée du
            // déplacement, relativement au nombre de pas par seconde
            // On incrémente la date de "temps" pas à chaque déplacement
            date = this.simulateur.getDateSimulationXSecondesApresDate(temps, date);
            LOGGER.info(
                    "Le robot " + robot + " se déplace de " + step.from + " à " + step.to + " à la date " + date);
            this.simulateur.ajouteEvenement(new DeplacementCase(date, robot, step.to));
        }
    }

    /**
     * Actions à effectuer lorsque le robot a atteint sa destination
     * 
     * @param robot Le robot sur lequel opérer
     */
    public void aAtteintDestination(Robot robot) {
        LOGGER.info("Le robot " + robot + " a atteint sa destination : " + robot.getPathDestination());
        Incendie incendie = this.donneesSimulation.getIncendieSurCase(robot.getPosition());

        // Si le robot est sur l'incendie qui lui a été affecté et qu'il n'est pas déjà
        // éteint, il intervient
        if (incendie != null && !incendie.estEteint() && getRobotsAffectesAIncendie(incendie) != null
                && getRobotsAffectesAIncendie(incendie).contains(robot)) {
            robot.setState(RobotState.DEVERSEMENT_EAU);

            double date = this.simulateur.getDateSimulationDansXSecondes(robot.getDureeIntervention());
            LOGGER.info("Le robot " + robot + " va intervenir sur l'incendie " + incendie + " à la date " + date);

            Intervention intervention = new Intervention(date, donneesSimulation, robot, incendie.getPosition());
            this.simulateur.ajouteEvenement(intervention);
        }
        // Sinon, si le robot peut se remplir, il se remplit
        else if (caseEstValidePourRemplissage(robot.getPosition(), robot)) {
            robot.setState(RobotState.RECHARGEMENT_OCCUPE);

            double date = this.simulateur.getDateSimulationDansXSecondes(robot.getDureeRemplissage());
            LOGGER.info("Le robot " + robot + " va se remplir à la date " + date);

            Remplissage remplissage = new Remplissage(date, donneesSimulation, robot);
            this.simulateur.ajouteEvenement(remplissage);
        }
        // Sinon, il attend de nouvelles instructions
        else {
            robot.setState(RobotState.ATTENTE);
            prendreDecision();
        }
    }

    /**
     * Renvoie si la case donnée est valide pour le remplissage du robot
     * 
     * @param c     La case à tester
     * @param robot Le robot à remplir
     * @return Vrai si la case est valide pour le remplissage, faux sinon
     */
    private boolean caseEstValidePourRemplissage(Case c, Robot robot) {
        // Si le robot est un drone, on vérifie s'il est au-dessus d'une case d'eau
        if (robot.getTypeRobot() == TypeRobot.DRONE) {
            return c.getTerrain() == NatureTerrain.EAU;
        }
        // Sinon, on vérifie si au moins une case adjacente est de l'eau
        else {
            // On s'assure de ne pas sortir de la carte
            int ligneSuperieure = Math.max(c.getLigne() - 1, 0);
            int ligneInferieure = Math.min(c.getLigne() + 1, this.donneesSimulation.getCarte().getLignes() - 1);
            int colonneGauche = Math.max(c.getColonne() - 1, 0);
            int colonneDroite = Math.min(c.getColonne() + 1, this.donneesSimulation.getCarte().getColonnes() - 1);

            for (int i = ligneSuperieure; i <= ligneInferieure; i++) {
                for (int j = colonneGauche; j <= colonneDroite; j++) {
                    if (i == c.getLigne() && j == c.getColonne()) {
                        continue;
                    }

                    if (this.donneesSimulation.getCarte().getCase(i, j).getTerrain() == NatureTerrain.EAU) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Actions à effectuer lorsque le robot n'a plus d'eau
     * 
     * @param robot Un robot qui n'a plus d'eau
     */
    public void reservoirVide(Robot robot) {
        Incendie incendie = this.donneesSimulation.getIncendieSurCase(robot.getPosition());

        if (incendie != null && getRobotsAffectesAIncendie(incendie) != null
                && getRobotsAffectesAIncendie(incendie).contains(robot)) {
            // robot.setState(incendie.estEteint() ? RobotState.RECHARGEMENT_LIBRE :
            // RobotState.RECHARGEMENT_OCCUPE);
            robot.setState(RobotState.RECHARGEMENT_OCCUPE);
        }

        Function<Case, Boolean> filtre_eau;

        filtre_eau = (c) -> caseEstValidePourRemplissage(c, robot);

        // On cherche un chemin vers la case d'eau la plus proche
        Path path = robot.getPathfinder().cheminAuPlusProche(robot, filtre_eau);

        if (path == null) {
            LOGGER.info("Le robot " + robot + " n'a pas trouvé de point d'eau");
            robot.setState(RobotState.ATTENTE);
            return;
        }

        Iterator<Case> it = path.getCases().iterator();

        // Si le robot est déjà à côté d'une source d'eau, le remplir
        if (!it.hasNext()) {
            LOGGER.info("Le robot " + robot + " est déjà à côté d'une source d'eau");
            double date = this.simulateur.getDateSimulationDansXSecondes(robot.getDureeRemplissage());

            Remplissage remplissage = new Remplissage(date, donneesSimulation, robot);
            this.simulateur.ajouteEvenement(remplissage);

            return;
        }

        // On récupère la dernière case du chemin
        Case c = it.next();
        while (it.hasNext()) {
            c = it.next();
        }

        // Si le robot est déjà à la destination
        if (robot.getPosition() == c) {
            LOGGER.info("Le robot " + robot + " est déjà à la source d'eau");
            this.aAtteintDestination(robot);
        }
        // Sinon, on programme le déplacement
        else {
            robot.setPathDestination(c);
            programmeDeplacement(robot, path);
        }
    }

    /**
     * Actions à effectuer lorsque le robot est rempli
     * 
     * @param robot
     */
    public void estRempli(Robot robot) {
        LOGGER.info("Le robot " + robot + " est rempli");
        robot.setState(RobotState.DEPLACEMENT_OCCUPE);

        Incendie incendie = getIncendieAffecteARobot(robot);

        if (incendie != null) {
            // S'il est éteint, on demande de nouvelles instructions au chef pompier
            if (incendie.estEteint()) {
                enleverAffectationRobot(robot);
                this.prendreDecision();
            }
            // Sinon, on se déplace vers l'incendie
            else {
                Path path = robot.getPathfinder().plusCourtChemin(robot, robot.getPosition(), incendie.getPosition());

                robot.setPathDestination(incendie.getPosition());
                programmeDeplacement(robot, path);
                return;
            }
        } else {
            // Si le robot n'a pas d'incendie à éteindre, on en cherche un autre
            robot.setState(RobotState.ATTENTE);
            this.prendreDecision();
        }
    }

    /**
     * Actions à effectuer lorsque l'incendie est éteint
     * 
     * @param incendie
     */
    public void incendieEteint(Incendie incendie) {
        LOGGER.info("L'incendie " + incendie + " est éteint");
        enleverAffectationsIncendie(incendie);
        this.prendreDecision();
    }

    /**
     * Réinitialise le chef pompier
     */
    public abstract void reinitialiser();
}