package tests;

import donnees.DonneesSimulation;
import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.robots.Robot;
import evenements.Evenement;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;
import gui.GUISimulator;
import io.LecteurDonnees;
import pathfinding.*;
import simulations.Simulateur;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.function.Function;
import java.util.zip.DataFormatException;

public class TestDijkstraFilter {
    public static void main(String[] args)
            throws FileNotFoundException, IllegalArgumentException, DataFormatException, RobotInconnu, NatureInconnue {

        DonneesSimulation donneesSimulation = LecteurDonnees.creeDonnees("cartes/spiralOfMadness-50x50.map");
        Carte carte = donneesSimulation.getCarte();

        int tailleMinimale = 20;
        int tailleEcranMax = Math.max(Toolkit.getDefaultToolkit().getScreenSize().height,
                Toolkit.getDefaultToolkit().getScreenSize().width);
        int largeur = Math.max(tailleEcranMax, tailleMinimale * carte.getColonnes());
        int hauteur = Math.max(tailleEcranMax, tailleMinimale * carte.getLignes());

        GUISimulator gui = new GUISimulator(largeur, hauteur, Color.BLUE);
        Simulateur simulateur = new Simulateur(gui, donneesSimulation, Color.WHITE, tailleMinimale);

        Pathfinder pathfinder = new Dijkstra(carte);
        Function<Case, Boolean> fire_filter = (c) -> donneesSimulation.getIncendieSurCase(c) != null;
        for (Robot robot : donneesSimulation.getRobots()) {
            Path path = pathfinder.cheminAuPlusProche(robot, fire_filter);
            if (path == null) {
                System.out.println("Pas de chemin pour " + robot);
                continue;
            }
            for (Evenement ev : path.toEvenements(0)) {
                simulateur.ajouteEvenement(ev);
            }
        }
    }
}
