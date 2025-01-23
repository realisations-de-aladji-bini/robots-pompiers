/*
 * TestScenario0.java

 */

package tests;

import donnees.robots.*;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import donnees.DonneesSimulation;
import donnees.enumerations.Direction;
import evenements.DeplacementDirection;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;
import simulations.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;

public class TestScenario0 {
    public static void main(String[] args)
            throws FileNotFoundException, IllegalArgumentException, DataFormatException, RobotInconnu, NatureInconnue {

        DonneesSimulation donneesSimulation = null;

        try {
            donneesSimulation = LecteurDonnees.creeDonnees("cartes/carteSujet.map");
        } catch (Exception e) {
            System.out.println("\n\t**Erreur: " + e.getMessage());
        }

        int tailleMinimale = 20;
        int tailleEcranMax = Math.max(Toolkit.getDefaultToolkit().getScreenSize().height,
                Toolkit.getDefaultToolkit().getScreenSize().width);
        int largeur = Math.max(tailleEcranMax, tailleMinimale * donneesSimulation.getCarte().getColonnes());
        int hauteur = Math.max(tailleEcranMax, tailleMinimale * donneesSimulation.getCarte().getLignes());

        /** Crée la fenêtre graphique dans laquelle dessiner */
        GUISimulator gui = new GUISimulator(largeur, hauteur, Color.BLUE);
        /** On crée un simulateur et on l'associe à la fenêtre gui */
        Simulateur simulateur = new Simulateur(gui, donneesSimulation, Color.WHITE, tailleMinimale);

        Robot robot = donneesSimulation.getRobot(0);

        DeplacementDirection deplacement1 = new DeplacementDirection(0, donneesSimulation, robot, Direction.NORD);
        DeplacementDirection deplacement2 = new DeplacementDirection(1, donneesSimulation, robot, Direction.NORD);
        DeplacementDirection deplacement3 = new DeplacementDirection(2, donneesSimulation, robot, Direction.NORD);
        DeplacementDirection deplacement4 = new DeplacementDirection(3, donneesSimulation, robot, Direction.NORD);

        simulateur.ajouteEvenement(deplacement1);
        simulateur.ajouteEvenement(deplacement2);
        simulateur.ajouteEvenement(deplacement3);
        simulateur.ajouteEvenement(deplacement4);
    }
}
