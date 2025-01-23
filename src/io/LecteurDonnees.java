/*
 * LecteurDonnees.java

 */

package io;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;
import donnees.carte.Case;
import donnees.carte.Carte;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import donnees.robots.RobotFactory;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;
import pathfinding.Dijkstra;
import pathfinding.Pathfinder;
import donnees.DonneesSimulation;
import donnees.enumerations.NatureTerrain;


/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affichées.
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des méthodes, inspirées de celles présentes
 * (ou non), qui CREENT les objets au moment adéquat pour construire une
 * instance de la classe DonneesSimulation à partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une méthode qui crée et retourne un objet
 * contenant toutes les données lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {


    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */
    public static void lire(String fichierDonnees)
            throws FileNotFoundException, DataFormatException, RobotInconnu {
        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        lecteur.lireCarte();
        lecteur.lireIncendies();
        lecteur.lireRobots();
        scanner.close();
        System.out.println("\n == Lecture terminee");
    }


    /**
     * Lit et renvoie un objet DonneesSimulation contenant les données lu dans le
     * fichier (cases, incendies et robots).
     * 
     * @param fichierDonnees nom du fichier à lire
     * 
     * @return Les données de la simulation lues dans le fichier
     * 
     * @throws FileNotFoundException
     * @throws DataFormatException
     * @throws IllegalArgumentException
     * @throws RobotInconnu
     * @throws NatureInconnue
     */
    public static DonneesSimulation creeDonnees(String fichierDonnees)
            throws FileNotFoundException, DataFormatException, IllegalArgumentException, RobotInconnu, NatureInconnue {
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);

        int[] tailleCarte = lecteur.getTailleCarte();
        int nbLignes = tailleCarte[0];
        int nbColonnes = tailleCarte[1];
        int tailleCases = tailleCarte[2];

        Case[][] cases = new Case[nbLignes][nbColonnes];

        for (int lig = 0; lig < nbLignes; lig++) {
            for (int col = 0; col < nbColonnes; col++) {
                cases[lig][col] = lecteur.creeCase(lig, col);
            }
        }

        Carte carte = new Carte(nbLignes, nbColonnes, cases, tailleCases);
        Pathfinder pathfinder = new Dijkstra(carte);

        LinkedList<Incendie> incendies = lecteur.creeIncendies(cases);
        LinkedList<Robot> robots = lecteur.creeRobots(cases, pathfinder);

        return new DonneesSimulation(carte, incendies, robots);
    }


    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private void lireCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt(); // en m
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    lireCase(lig, col);
                }
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }

    /**
     * Lit et renvoie les dimensions de la carte.
     * 
     * @return Un tableau contenant 3 valeurs: le nombre de lignes, le nombre de
     *         colonnes et la taille des cases
     * 
     * @throws DataFormatException
     */
    private int[] getTailleCarte() throws DataFormatException {
        ignorerCommentaires();

        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt(); // en m

            return new int[] { nbLignes, nbColonnes, tailleCases };
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
    }



    /**
     * Lit et affiche les donnees d'une case.
     */
    private void lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Case (" + lig + "," + col + "): ");
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);

            verifieLigneTerminee();

            System.out.print("nature = " + chaineNature);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }

        System.out.println();
    }


    /**
     * Crée et renvoie un objet Case à partir de la nature du terrain lue
     * 
     * @param lig La ligne de la case
     * @param col La colonne de la case
     * @return La case créée
     * @throws DataFormatException
     * @throws NatureInconnue
     */
    private Case creeCase(int lig, int col) throws DataFormatException, NatureInconnue {
        ignorerCommentaires();

        try {
            Case caseCree = new Case(lig, col, NatureTerrain.valueOf(scanner.next()));

            verifieLigneTerminee();

            return caseCree;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        } catch (IllegalArgumentException e) {
            throw new NatureInconnue("nature de terrain inconnue");
        }
    }

    /**
     * Lit et affiche les donnees des incendies.
     */
    private void lireIncendies() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                lireIncendie(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private void lireIncendie(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Crée et renvoie une liste d'incendies à partir des données lues dans le
     * fichier.
     * 
     * @param cases Une matrice de cases lues depuis le fichier
     * 
     * @return La liste des incendies créés
     * 
     * @throws DataFormatException
     */
    private LinkedList<Incendie> creeIncendies(Case[][] cases) throws DataFormatException {
        ignorerCommentaires();

        try {
            int nbIncendies = scanner.nextInt();
            LinkedList<Incendie> incendies = new LinkedList<>();

            for (int i = 0; i < nbIncendies; i++) {
                incendies.add(creeIncendie(cases, i));
            }

            return incendies;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }

    /**
     * Crée et renvoie un objet Incendie à partir des données lues dans le fichier.
     * 
     * @param cases La matrice de cases lues depuis le fichier
     * @param i     Le numéro de l'incendie
     * 
     * @return L'incendie créé
     * 
     * @throws DataFormatException
     */
    private Incendie creeIncendie(Case[][] cases, int i) throws DataFormatException {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();

            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }

            verifieLigneTerminee();

            return new Incendie(cases[lig][col], intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }

    /**
     * Lit et affiche les donnees des robots.
     */
    private void lireRobots() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                lireRobot(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private void lireRobot(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            System.out.print("\t type = " + type);

            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)"); // 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                int vitesse = Integer.parseInt(s);
                System.out.print(vitesse);
            }
            verifieLigneTerminee();

            System.out.println();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }


    /**
     * Crée et renvoie une liste de robots à partir des données lues dans le
     * fichier.
     * 
     * @param cases La matrice de cases lues depuis le fichier
     * 
     * @return La liste des robots créés
     * 
     * @throws DataFormatException
     * @throws RobotInconnu
     */
    private LinkedList<Robot> creeRobots(Case[][] cases, Pathfinder pathfinder)
            throws DataFormatException, RobotInconnu {
        ignorerCommentaires();

        try {
            int nbRobots = scanner.nextInt();
            LinkedList<Robot> robots = new LinkedList<>();

            for (int i = 0; i < nbRobots; i++) {
                robots.add(creeRobot(cases, i, pathfinder));
            }

            return robots;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }

    /**
     * Crée et renvoie un objet Robot à partir des données lues dans le fichier.
     * 
     * @param cases La matrice de cases lues depuis le fichier
     * @param i     Le numéro du robot
     * 
     * @return Le robot créé
     * 
     * @throws DataFormatException
     * @throws RobotInconnu
     */
    private Robot creeRobot(Case[][] cases, int i, Pathfinder pathfinder) throws DataFormatException, RobotInconnu {
        ignorerCommentaires();

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            String type = scanner.next();

            // lecture eventuelle d'une vitesse du robot (entier)
            String s = scanner.findInLine("(\\d+(\\.\\d+)?)");

            double vitesse = -1;

            if (s != null) {
                vitesse = Double.parseDouble(s);
            }

            verifieLigneTerminee();

            return RobotFactory.creeRobot(type, cases[lig][col], vitesse, pathfinder);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }


    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
