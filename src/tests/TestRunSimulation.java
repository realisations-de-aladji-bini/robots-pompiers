package tests;

import java.awt.*;

import chefpompiers.ChefPompier;
import chefpompiers.ChefPompierElementaire;
import chefpompiers.ChefPompierEvolue;
import donnees.DonneesSimulation;
import gui.GUISimulator;
import io.LecteurDonnees;
import io.logging.SimulationLogging;
import simulations.Simulateur;
import simulations.StrategieSimulation;

public class TestRunSimulation {
    public static void main(String[] args) {

        String nomCarte = "carteSujet.map";
        StrategieSimulation strategie = StrategieSimulation.EVOLUE;

        if (args.length > 2) {
            System.out.println("\n\tErreur: Trop d'arguments");
            System.out.println("\tUsage: make exeRunSimulation [carte=<carte>] [strategie=<strategie>]");
            System.out.println(
                    "\t[carte=<carte>] (facultatif): Nom de la carte à utiliser (nom du fichier + extension, sans le chemin)");
            System.out.println(
                    "\t[strategie=<strategie>] (facultatif): Stratégie de simulation à utiliser (elementaire ou evolue)");
            System.out.println("\tExemple: make exeRunSimulation carte=carteSujet.map strategie=evolue");
        }

        // On découpe les arguments pour trouver la carte et la stratégie éventuelle
        for (String arg : args) {
            String[] argSplit = arg.split("=");

            if (argSplit.length != 2) {
                continue;
            }

            // On met à jour les variables en fonction des arguments passés
            if (argSplit[0].equals("carte")) {
                nomCarte = argSplit[1];
            } else if (argSplit[0].equals("strategie")) {
                if (argSplit[1].equals("elementaire")) {
                    strategie = StrategieSimulation.ELEMENTAIRE;
                } else if (argSplit[1].equals("evolue")) {
                    strategie = StrategieSimulation.EVOLUE;
                }
            }
        }

        System.out.println("\n\tCarte: " + nomCarte);
        System.out.println("\tStratégie: " + strategie + "\n");

        DonneesSimulation donneesSimulation = null;

        try {
            donneesSimulation = LecteurDonnees.creeDonnees("cartes/" + nomCarte);
        } catch (Exception e) {
            System.err.println("Erreur: " + e);
            System.exit(1);
        }

        int tailleMinimale = 20;
        int tailleEcranMax = Math.max(Toolkit.getDefaultToolkit().getScreenSize().height,
                Toolkit.getDefaultToolkit().getScreenSize().width);
        int largeur = Math.max(tailleEcranMax, tailleMinimale * donneesSimulation.getCarte().getColonnes());
        int hauteur = Math.max(tailleEcranMax, tailleMinimale * donneesSimulation.getCarte().getLignes());

        // Crée la fenêtre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(largeur, hauteur, Color.BLUE);

        // On crée un simulateur et on l'associe à la fenêtre gui
        Simulateur simulateur = new Simulateur(gui, donneesSimulation, Color.WHITE, tailleMinimale);

        // Modifie tous les loggers pour afficher le temps de simulation
        SimulationLogging.setup(simulateur);

        ChefPompier chefPompier = null;
        if (strategie == StrategieSimulation.ELEMENTAIRE) {
            chefPompier = new ChefPompierElementaire(donneesSimulation, simulateur);
        } else if (strategie == StrategieSimulation.EVOLUE) {
            chefPompier = new ChefPompierEvolue(donneesSimulation, simulateur);
        }

        if (chefPompier != null) {
            simulateur.associerChefPompier(chefPompier);
        }
    }
}
