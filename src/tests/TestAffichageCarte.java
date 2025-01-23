/*
 * TestAffichageCarte.java

 */

package tests;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import donnees.DonneesSimulation;
import simulations.Simulateur;
import gui.GUISimulator;
import io.LecteurDonnees;

/**
 * Fichier de test qui permet de s'assurer que le fichier .map passé en
 * paramètre
 * est bien désérialisé et affiché
 */
public class TestAffichageCarte {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestAffichageCarte <nomDeFichier>");
            System.exit(1);
        }

        DonneesSimulation donneesSimulation = null;

        try {
            donneesSimulation = LecteurDonnees.creeDonnees(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
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
    }
}
