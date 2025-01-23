/*
 * TestScenario1.java

 */

package tests;

 import java.awt.Color;
 import java.awt.Toolkit;
 import java.io.FileNotFoundException;
 import java.util.zip.DataFormatException;
 import donnees.robots.*;
import donnees.DonneesSimulation;
import donnees.enumerations.Direction;
import evenements.DeplacementDirection;
import evenements.Intervention;
import evenements.Remplissage;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;
import simulations.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;

public class TestScenario1 {
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

        Robot robot = donneesSimulation.getRobot(1);

        DeplacementDirection deplacement1 = new DeplacementDirection(0, donneesSimulation, robot, Direction.NORD);

        DeplacementDirection deplacement2 = new DeplacementDirection(52, donneesSimulation, robot, Direction.OUEST);
        DeplacementDirection deplacement3 = new DeplacementDirection(53, donneesSimulation, robot, Direction.OUEST);

        Remplissage remplissage = new Remplissage(54, donneesSimulation, robot);

        DeplacementDirection deplacement5 = new DeplacementDirection(55, donneesSimulation, robot, Direction.EST);
        DeplacementDirection deplacement6 = new DeplacementDirection(56, donneesSimulation, robot, Direction.EST);

        simulateur.ajouteEvenement(deplacement1);

        for (int i = 1; i < 51; i++) {
            simulateur.ajouteEvenement(
                    new Intervention(i, donneesSimulation, robot, donneesSimulation.getCarte().getCase(5, 5)));
        }

        simulateur.ajouteEvenement(deplacement2);
        simulateur.ajouteEvenement(deplacement3);
        simulateur.ajouteEvenement(remplissage);
        simulateur.ajouteEvenement(deplacement5);
        simulateur.ajouteEvenement(deplacement6);

        for (int i = 57; i < 107; i++) {
            simulateur.ajouteEvenement(
                    new Intervention(i, donneesSimulation, robot, donneesSimulation.getCarte().getCase(5, 5)));
        }
    }
}
