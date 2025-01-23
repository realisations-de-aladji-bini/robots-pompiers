/*
 * TestDijkstra.java

 */

package tests;

import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.DonneesSimulation;
import donnees.robots.Robot;
import evenements.*;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;
import simulations.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;
import pathfinding.Dijkstra;
import pathfinding.Path;
import pathfinding.Pathfinder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestDijkstra {
    public static void main(String[] args)
            throws FileNotFoundException, IllegalArgumentException, DataFormatException, RobotInconnu, NatureInconnue {

        DonneesSimulation donneesSimulation = null;

        try {
            donneesSimulation = LecteurDonnees.creeDonnees("cartes/mushroomOfHell-20x20.map");
        } catch (Exception e) {
            System.out.println("\n\t**Erreur: " + e.getMessage());
        }

        Carte carte = donneesSimulation.getCarte();

        int tailleMinimale = 20;
        int tailleEcranMax = Math.max(Toolkit.getDefaultToolkit().getScreenSize().height,
                Toolkit.getDefaultToolkit().getScreenSize().width);
        int largeur = Math.max(tailleEcranMax, tailleMinimale * carte.getColonnes());
        int hauteur = Math.max(tailleEcranMax, tailleMinimale * carte.getLignes());

        GUISimulator gui = new GUISimulator(largeur, hauteur, Color.BLUE);
        Simulateur simulateur = new Simulateur(gui, donneesSimulation, Color.WHITE, tailleMinimale);

        Robot robot = donneesSimulation.getRobot(1);

        Pathfinder pathfinder = new Dijkstra(carte);
        Path path = pathfinder.plusCourtChemin(robot, robot.getPosition(), donneesSimulation.getCarte().getCase(7, 0));

        int date = 0;
        for (Case step : path.getCases()) {
            Evenement ev = new DeplacementCase(date++, robot, step);
            simulateur.ajouteEvenement(ev);
        }
    }
}
