/*
 * PathStep.java

 */

package pathfinding;

import donnees.carte.Case;

/**
 * Deux cases adjacentes représentant une étape d'un chemin
 */
public class PathStep {
    public Case from;
    public Case to;

    public PathStep(Case from, Case to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "from: " + from + " to: " + to;
    }
}
