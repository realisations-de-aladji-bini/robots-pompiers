/*
 * Robot.java

 */

package donnees.robots;

import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;
import donnees.incendie.Incendie;
import pathfinding.Pathfinder;
import chefpompiers.ChefPompier;

public abstract class Robot implements Cloneable {
    private Case position;
    private double vitesse;
    private double vitesseMax;
    private int reservoir; // La capacité actuelle du réservoir du robot
    private TypeRobot typeRobot; // Utile pour afficher le type du robot pour debug
    private RobotState state;
    private Pathfinder pathfinder;
    private ChefPompier chefPompier;
    private Case pathDestination;
    private int tempsRemplissage; // Temps en secondes
    private int tempsIntervention; // Temps en secondes
    private int volumeIntervention; // Volume en Litres
    private int capaciteReservoir; // Volume en Litres
    private String image;

    /**
     * Constructeur de la classe Robot
     *
     * @param typeRobot Le type du robot
     */
    public Robot(TypeRobot typeRobot, int vitesseMax, int tempsRemplissage, int tempsIntervention,
            int capaciteReservoir,
            int volumeIntervention, String image) {
        this.typeRobot = typeRobot;
        this.state = RobotState.ATTENTE;

        this.vitesseMax = vitesseMax;
        this.tempsRemplissage = tempsRemplissage;
        this.tempsIntervention = tempsIntervention;
        this.capaciteReservoir = capaciteReservoir;
        this.volumeIntervention = volumeIntervention;
        this.image = image;
    }

    /**
     * Renvoie la vitesse de déplacement du robot en fonction de la nature d’un
     * terrain.
     *
     * @param terrain La nature du terrain sur lequel se déplace le robot
     * @return La vitesse en m/s du robot sur le terrain courant.
     */
    public abstract double getVitesseDeplacement(NatureTerrain terrain);

    /**
     * Renvoie l'image d'un robot pour l'affichage sur le graphique
     *
     * @return Le chemin de l'image
     */
    public String getImage() {
        return image;
    }

    /**
     * Retourne le type du robot courant
     *
     * @return Le type du robot courant
     */
    public TypeRobot getTypeRobot() {
        return this.typeRobot;
    }

    /**
     * Méthode d'intervention d'un robot sur un incendie
     * Déverse le volume d'eau par défaut d'un robot
     */
    public void deverserEau() {
        this.deverserEau(Math.min(this.getVolumeIntervention(), this.reservoir));
    }

    /**
     * Méthode d'intervention d'un robot sur un incendie
     *
     * @param volume Le volume d'eau à deverser
     */
    public void deverserEau(int volume) {
        int capacite = this.getReservoir();

        if (capacite - volume < 0) {
            throw new IllegalArgumentException("Le volume d'eau ne doit pas être supérieur à " + capacite + " Litres");
        }

        this.setReservoir(capacite - volume);
    }

    /**
     * Renvoie la capacité du réservoir d'eau du robot
     *
     * @return La capacité du réservoir en Litres
     */
    public int getCapaciteReservoir() {
        return capaciteReservoir;
    }

    /**
     * Le volume d'intervention en Litres
     *
     * @return Le volume d'intervention en Litres
     */
    public int getVolumeIntervention() {
        return volumeIntervention;
    }

    /**
     * La durée d'intervention en secondes
     *
     * @return La durée nécessaire d'intervention
     */
    public int getDureeIntervention() {
        return tempsIntervention;
    }

    /**
     * Le temps de remplissage du réservoir
     *
     * @return Le temps de remplissage du réservoir
     */
    public int getDureeRemplissage() {
        return tempsRemplissage;
    }

    /**
     * Retourne la case occupée par le robot
     *
     * @return La position courante du robot
     */
    public Case getPosition() {
        return position;
    }

    /**
     * Met à jour la position du robot. En d'autres termes, cette méthode d&place le
     * robot
     *
     * @param nouvellePosition La nouvelle position (case) que va occuper le robot
     */
    public void setPosition(Case nouvellePosition) {
        this.position = nouvellePosition;
    }

    /**
     * Renvoie simplement la vitesse sans tenir compte de la nature du terrain
     *
     * @return La vitesse actuelle du robot en m/s
     */
    public double getVitesse() {
        return this.vitesse;
    }

    /**
     * Modifie la vitesse du robot
     *
     * @param v La nouvelle vitesse en km/h
     * @throw Lève une exception si la vitesse fournie est négative
     */
    public void setVitesse(double v) {
        if (v < 0 || v > vitesseMax) {
            throw new IllegalArgumentException("La vitesse doit être comprise entre 0 et " + vitesseMax + " km/h");
        }

        this.vitesse = v / 3.6; // On convertit la vitesse en m/s
    }

    /**
     * Calcule le temps de déplacement entre deux cases adjacentes
     *
     * @param from La case de départ
     * @param to   La case d'arrivée
     * @return Le temps de déplacement entre les deux cases en secondes (s)
     */
    public double tempsDeDeplacement(Case from, Case to, Carte carte) {
        int demiCase = carte.getTailleCase() / 2;
        double vitesse1 = this.getVitesseDeplacement(from.getTerrain());
        assert vitesse1 > 0;
        double temps1 = demiCase / vitesse1;
        double vitesse2 = this.getVitesseDeplacement(to.getTerrain());
        assert vitesse2 > 0;
        double temps2 = demiCase / vitesse2;
        return temps1 + temps2;
    }

    /**
     * Renvoie le volume du réservoir
     *
     * @return Le volume du réservoir
     */
    public int getReservoir() {
        return reservoir;
    }

    /**
     * Met à jour le volume du réservoir
     *
     * @param volume Le nouveau volume du réservoir
     */
    public void setReservoir(int volume) {
        if (volume < 0 || volume > capaciteReservoir) {
            throw new IllegalArgumentException(
                    "Le volume du resrvoir doit être comprise entre 0 et " + capaciteReservoir + " Litres");
        }
        reservoir = volume;
    }

    /**
     * Remplit le réservoir complet du robot avec sa capacité totale
     */
    public void remplirReservoir() {
        this.reservoir = getCapaciteReservoir();

        if (this.chefPompier != null) {
            this.chefPompier.estRempli(this);
        }
    }

    /**
     * Affichage textuel d'un robot avec ses caractéristiques
     *
     * @return La représentation textuelle du robot
     */
    @Override
    public String toString() {
        return String.format(
                "%s { %s, @%s, %.1fm/s, %dL }",
                this.typeRobot,
                this.state,
                this.getPosition(),
                this.getVitesse(),
                this.getReservoir()
        );
    }

    /**
     * Crée une copie (clone) du robot courant
     */
    public Object clone() {
        try {
            Robot robot = (Robot) super.clone();
            robot.vitesse = this.vitesse;
            robot.setPosition(this.getPosition());

            return robot;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recopie les propriétés du robot passé en paramètre dans this
     * Permet de cloner un robot mais en gardant la référence de l'objet
     *
     * @param robot Les propriétés à recopier
     */
    public void recopierProprietes(Robot robot) {
        this.vitesse = robot.getVitesse();
        this.setPosition(robot.getPosition());
        this.setReservoir(robot.getReservoir());
        this.state = RobotState.ATTENTE;
    }

    /**
     * Modifie le chef pompier du robot
     *
     * @param chefPompier
     */
    public void setChefPompier(ChefPompier chefPompier) {
        this.chefPompier = chefPompier;
    }

    /**
     * Renvoie le chef pompier du robot
     *
     * @return Le chef pompier du robot
     */
    public ChefPompier getChefPompier() {
        return this.chefPompier;
    }

    /**
     * Modifie le pathfinder utilisé par le robot
     *
     * @param pathfinder
     */
    public void setPathfinder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;
    }

    /**
     * Renvoie le pathfinder du robot
     *
     * @return Le pathfinder du robot
     */
    public Pathfinder getPathfinder() {
        return this.pathfinder;
    }

    /**
     * Modifie la destination du robot
     *
     * @param pathDestination
     */
    public void setPathDestination(Case pathDestination) {
        this.pathDestination = pathDestination;
    }

    /**
     * Renvoie la destination du robot
     * 
     * @return La destination du robot
     */
    public Case getPathDestination() {
        return this.pathDestination;
    }

    /**
     * Modifie le state du robot
     *
     * @param state
     */
    public void setState(RobotState state) {
        this.state = state;
    }

    /**
     * Renvoie le state du robot
     *
     * @return
     */
    public RobotState getState() {
        return this.state;
    }

    /**
     * Renvoie si le robot est déjà en train d'intervenir sur un incendie
     * (déplacement, déversement d'eau ou rechargement)
     *
     * @return true si le robot est déjà en train d'intervenir, false sinon
     */
    public boolean estOccupe() {
        return this.state != RobotState.ATTENTE
                && this.state != RobotState.DEPLACEMENT_LIBRE
                && this.state != RobotState.RECHARGEMENT_LIBRE;
    }

    /**
     * Fait intervenir le robot sur l'incendie donné
     *
     * @param incendie
     */
    public void intervenir(Incendie incendie) {
        this.state = RobotState.DEPLACEMENT_OCCUPE;
        this.pathDestination = incendie.getPosition();
    }

    /**
     * Appelé lorsque le robot se déplace, s'il atteint sa destination, il notifie
     * le chef pompier
     */
    public void avancer() {
        if (pathDestination != null && this.getPosition().equals(pathDestination)) {
            this.pathDestination = null;
            this.chefPompier.aAtteintDestination(this);
        }
    }
}
