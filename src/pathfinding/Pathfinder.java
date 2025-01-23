/*
 * Pathfinder.java

 */

package pathfinding;

import donnees.robots.Robot;
import donnees.carte.Carte;
import donnees.carte.Case;

import java.util.function.Function;

public abstract class Pathfinder {
    Carte carte;

    /**
     * Calcule et retourne le chemin le moins long d'une case à une autre pour le robot donné,
     * prenant en compte sa vitesse et sa compatibilité avec les différents types de terrain.
     *
     * @param robot Le robot qui va utiliser ce chemin
     * @param src   Case de départ
     * @param dst   Case d'arrivée
     * @return Le chemin le plus rapide s'il existe sinon null
     */
    public abstract Path plusCourtChemin(Robot robot, Case src, Case dst);

    /**
     * Calcule et retourne le chemin le moins long pour le robot donné,
     * vers un type de destination contrôlé par une fonction de sélection.
     *
     * @param robot Le robot qui va utiliser ce chemin
     * @param selector Filtre pour sélectionner les cases qualifiées comme destination.
     *                 Doit retourner true si la case fait partie des destinations acceptées.
     * @return Le chemin le plus rapide s'il existe sinon null
     */
    public abstract Path cheminAuPlusProche(Robot robot, Function<Case, Boolean> selector);
}