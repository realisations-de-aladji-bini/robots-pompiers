/*
 * Simulateur.java

 */

package simulations;

import gui.GUISimulator;
import gui.ImageElement;
import gui.Rectangle;
import gui.Simulable;
import java.awt.Color;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import chefpompiers.ChefPompier;
import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.DonneesSimulation;
import donnees.incendie.Incendie;
import donnees.robots.Robot;
import evenements.DecisionChefPompier;
import evenements.Evenement;

public class Simulateur implements Simulable {
    // Config
    public final double DUREE_PAS_SIMULATION = 1.0; // Le temps en secondes qui s'écoule dans la simulation entre deux
                                                    // pas.
    private final DonneesSimulation donneesInitiales; // Utilisé pour la réinitialisation. Constant : ces données ne sont pas modifiées
    private final int volumeMaxEau; // Permet de calculer dynamiquement les dimensions de l'image de l'incendie
    private final int tailleMinimale;

    // Affichage
    private final GUISimulator gui;
    private final Color couleurBordure;
    private final DonneesSimulation donnees;

    // Logging
    private static final Logger LOGGER = Logger.getLogger("Simulateur");

    // State
    private ChefPompier chefPompier;
    private boolean simulationFinie = false;
    private double dateSimulation;

    /**
     * Liste des évènements à exécuter. On utilise une PriorityQueue pour
     * pouvoir gérer l'ordonnancement des évènements en fonction de leur date
     * de manière efficace (O(log n) pour ajouter/supprimer contre O(n) si on
     * avait utilisé une LinkedList par exemple).
     */
    private final PriorityQueue<Evenement> evenements;

    /**
     * Liste des évènements déjà exécutés, nécessaire pour pouvoir redémarrer la
     * simulation. On peut cette fois utiliser une Linked list (insertion en O(1))
     * car les évènements sont ajoutés au fur et à mesure qu'ils sont exécutés (donc
     * déjà ordonnés).
     */
    private final LinkedList<Evenement> evenementsExecutes;

    public Simulateur(GUISimulator gui, DonneesSimulation donnees, Color bordure, int taille) {
        this.gui = gui;
        this.donnees = donnees;
        this.donneesInitiales = donnees.clonerDonnees();
        this.couleurBordure = bordure;
        this.tailleMinimale = taille;
        this.volumeMaxEau = donnees.getVolumeEauNecessaire();

        this.evenements = new PriorityQueue<>();
        this.evenementsExecutes = new LinkedList<>();
        this.dateSimulation = 0;

        gui.setSimulable(this);
        draw();
    }


    /**
     * Affiche l'état actuel de la simulation dans l'interface graphique.
     */
    private void draw() {
        gui.reset();
        Carte carte = donnees.getCarte();
        int lignes = carte.getLignes();
        int colonnes = carte.getColonnes();

        // On adapte l'affichage selon la taille de la fenêtre

        int largeurCase = gui.getWidth() / lignes;
        int hauteurCase = (gui.getHeight() - 75) / colonnes;

        int tailleCase = Math.max(Math.min(largeurCase, hauteurCase), tailleMinimale);

        for (int l = 0; l < lignes; l++) {
            for (int c = 0; c < colonnes; c++) {
                Case caseCourante = carte.getCase(l, c);
                int x = caseCourante.getColonne() * tailleCase;
                int y = caseCourante.getLigne() * tailleCase;

                gui.addGraphicalElement(new ImageElement(x, y, caseCourante.getTerrain().getImage(), tailleCase, tailleCase, null));
                gui.addGraphicalElement(new Rectangle(x + tailleCase / 2, y + tailleCase / 2, couleurBordure, null, tailleCase, tailleCase));
            }
        }

        // On affiche les incendies
        for (Incendie i : donnees.getIncendies()) {
            if (i.estEteint()) {
                continue;
            }

            int tailleIncendie = tailleCase / 2 + (tailleCase * i.getEauNecessaire()) / (volumeMaxEau * 2);
            int x = i.getPosition().getColonne() * tailleCase + (tailleCase - tailleIncendie) / 2;
            int y = i.getPosition().getLigne() * tailleCase + (tailleCase - tailleIncendie) / 2;
            gui.addGraphicalElement(new ImageElement(x, y, i.getImage(), 2 * tailleCase / 3, 2 * tailleCase / 3, null));
        }

        // Affichage des robots
        for (Robot robot : donnees.getRobots()) {
            int x = robot.getPosition().getColonne() * tailleCase + tailleCase / 6;
            int y = robot.getPosition().getLigne() * tailleCase + tailleCase / 6;
            gui.addGraphicalElement(new ImageElement(x, y, robot.getImage(), 2 * tailleCase / 3, 2 * tailleCase / 3, null));
        }
    }


    /**
     * Méthode qui passe au pas suivant de la simulation.
     * Elle est invoquée suite à un clic sur le bouton Suivant, ou bien à intervalles réguliers si la
     * lecture a été démarrée.
     */
    @Override
    public void next() {
        if (simulationTerminee()) {
            if (simulationFinie) {
                return;
            }

            simulationFinie = true;
            LOGGER.info("Tous les incendies ont été éteints");
            LOGGER.info("Fin de la simulation");
            return;
        }

        // On exécute tous les évènements qui ont une date inférieure ou égale à la date
        // de simulation
        // Peek permet de récupérer l'évènement avec la date la plus petite sans le
        // supprimer de la queue
        // Poll permet de récupérer et supprimer l'évènement avec la date la plus petite
        while (!simulationTerminee() && evenements.size() > 0 && evenements.peek().getDate() <= dateSimulation) {
            Evenement e = evenements.poll();

            LOGGER.info("Évènement exécuté: " + e);

            e.execute();

            this.evenementsExecutes.addLast(e);
        }

        incrementeDate();

        draw();
    }

    /**
     * Invoquée suite à un clic sur le bouton Début, la lecture est alors arrêtée, et le
     * simulateur revient dans l’état initial.
     */
    @Override
    public void restart() {
        this.dateSimulation = 0;

        donnees.recopierProprietesDonnees(donneesInitiales);

        // Si le chef pompier est null, les évènements ont été pré-programmés et doivent
        // alors être remis dans la file pour être exécutés, sinon on peut les supprimer
        // et redémarrer la simulation en ajoutant simplement un évènement de décision
        // du chef pompier.
        if (this.chefPompier == null) {
            for (Evenement e : this.evenementsExecutes) {
                this.ajouteEvenement(e);
            }

            this.evenementsExecutes.clear();
        } else {
            evenements.clear();

            this.chefPompier.reinitialiser();
            this.ajouteEvenement(new DecisionChefPompier(0, this.chefPompier));
        }

        this.simulationFinie = false;

        LOGGER.info("\nSimulation redémarrée\n");

        draw();
    }

    /**
     * Ajoute l'évènement donné à la simulation
     *
     * @param e L'évènement à ajouter
     * @throws IllegalArgumentException
     */
    public void ajouteEvenement(Evenement e) {
        if (!evenements.offer(e)) {
            throw new IllegalArgumentException("Impossible d'ajouter l'événement" + e + " à la simulation");
        }
    }

    /**
     * Incrémente le pas de temps de la simulation
     */
    private void incrementeDate() {
        dateSimulation += DUREE_PAS_SIMULATION;
    }

    /**
     * Renvoie la date actuelle de la simulation
     *
     * @return La date actuelle de la simulation
     */
    public double getDateSimulation() {
        return dateSimulation;
    }

    /**
     * Renvoie la date de simulation (nombre de pas) dans x secondes relativement au
     * nombre de pas par seconde
     *
     * @param x
     * @return La date de simulation dans x secondes
     */
    public double getDateSimulationDansXSecondes(double x) {
        return getDateSimulationXSecondesApresDate(x, this.getDateSimulation());
    }

    /**
     * Renvoie la date de simulation (nombre de pas) x secondes après la
     * dateSimulation donnée relativement au nombre de pas par seconde
     *
     * @param x
     * @param dateSimulation
     * @return La date de simulation dans x secondes après dateSimulation
     */
    public double getDateSimulationXSecondesApresDate(double x, double dateSimulation) {
        return dateSimulation + (x * (1 / DUREE_PAS_SIMULATION));
    }

    /**
     * Renvoie vrai si la simulation est terminée (plus d'évènements à exécuter) et
     * que tous les incendies sont éteints ou false sinon
     *
     * @return true si la simulation est terminée, false sinon
     */
    private boolean simulationTerminee() {
        for (Incendie incendie : donnees.getIncendies()) {
            if (!incendie.estEteint()) {
                return false;
            }
        }

        return evenements.isEmpty();
    }

    /**
     * Associe un chef pompier à la simulation
     *
     * @param chefPompier
     */
    public void associerChefPompier(ChefPompier chefPompier) {
        this.chefPompier = chefPompier;
    }

    /**
     * Affiche les évènements de la simulation dans l'ordre de date d'exécution.
     * Utile pour débugger.
     * Attention : La liste d'évènements est vide après l'appel à cette méthode et
     * ne peut donc plus être utilisée (mais c'est le seul moyen de récupérer les
     * évènements dans l'ordre)
     */
    public void afficheEvenements() {
        while (!evenements.isEmpty()) {
            LOGGER.info(evenements.poll().toString());
        }
    }
}
