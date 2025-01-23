/*
 * Dijkstra.java

 */

package pathfinding;

import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.robots.Robot;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Contexte pour une recherche de chemin utilisant l'algorithme de Dijkstra.
 */
class DijkstraContexte {
    HashMap<Case, Double> distance;
    HashMap<Case, Case> precedent;
    PriorityQueue<Case> queue;

    Carte carte;

    private DijkstraContexte(Carte carte) {
        this.carte = carte;
        int taille = carte.getLignes() * carte.getColonnes();
        this.distance = new HashMap<>(taille);
        this.precedent = new HashMap<>();
        this.queue = new PriorityQueue<>(taille, Comparator.comparingDouble(this::getDistance));
        for (int lig = 0; lig < carte.getLignes(); lig++) {
            for (int col = 0; col < carte.getColonnes(); col++) {
                this.distance.put(carte.getCase(lig, col), Double.POSITIVE_INFINITY);
                this.queue.add(carte.getCase(lig, col));
            }
        }
    }

    DijkstraContexte(Carte carte, Case src) {
        this(carte);
        this.setDistance(src, 0);
        this.queue.add(src);
    }

    void setDistance(Case case_, double dist) {
        this.distance.put(case_, dist);
    }

    double getDistance(Case case_) {
        return this.distance.get(case_);
    }

    void setPrecedent(Case case_, Case prev) {
        this.precedent.put(case_, prev);
    }

    Case getPrecedent(Case case_) {
        return this.precedent.get(case_);
    }

    /*
     * Construit le chemin de src à dst.
     * Précondition : l'algorithme de Dijkstra doit avoir été executé au moins
     * jusqu'à avoir atteint la destination.
     */
    Path construireChemin(Case src, Case dst, Robot robot) {
        Path path = new Path(robot);
        if (this.getPrecedent(dst) != null || dst.equals(src)) {
            while (dst != null) {
                Case prev = this.getPrecedent(dst);
                double coutTotal = this.getDistance(dst);
                double precedentCoutTotal = prev != null ? this.getDistance(prev) : 0;
                double coutRelatif = coutTotal - precedentCoutTotal;
                path.ajouterDebut(dst, coutRelatif);
                dst = prev;
            }
        }
        return path;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int lig = 0; lig < this.carte.getLignes(); lig++) {
            for (int col = 0; col < this.carte.getColonnes(); col++) {
                s.append(String.format("%1.6f", this.distance.get(this.carte.getCase(lig, col))));
                s.append(" ");
            }
            s.append("\n");
        }
        s.append("\n");
        for (int lig = 0; lig < this.carte.getLignes(); lig++) {
            for (int col = 0; col < this.carte.getColonnes(); col++) {
                s.append(this.getPrecedent(this.carte.getCase(lig, col)));
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}

/**
 * Recherche de chemin utilisant l'algorithme de Dijkstra.
 */
public class Dijkstra extends Pathfinder {

    private static final Logger LOGGER = Logger.getLogger("Pathfinder");

    /**
     * Construit un contexte pour rechercher les plus courts chemins sur une carte.
     *
     * @param carte La carte sur laquelle calculer les chemins
     */
    public Dijkstra(Carte carte) {
        this.carte = carte;
    }

    @Override
    public Path plusCourtChemin(Robot robot, Case src, Case dst) {
        LOGGER.info("Recherche de chemin pour %s, de %s vers %s".formatted(robot, src, dst));

        DijkstraContexte ctx = new DijkstraContexte(carte, src);
        boolean found = this.runSearch(ctx, robot, dst::equals, ignored -> false);
        if (!found) {
            return null;
        }
        return ctx.construireChemin(src, dst, robot);
    }

    @Override
    public Path cheminAuPlusProche(Robot robot, Function<Case, Boolean> selector) {
        LOGGER.info("Recherche de chemin pour %s".formatted(robot));

        final Case src = robot.getPosition();
        AtomicReference<Case> nearestCandidate = new AtomicReference<>();
        DijkstraContexte ctx = new DijkstraContexte(carte, src);
        boolean found = this.runSearch(ctx, robot, selector, (n) -> {
            if (nearestCandidate.get() == null || ctx.getDistance(n) < ctx.getDistance(nearestCandidate.get())) {
                nearestCandidate.set(n);
            }
            return true;
        });
        if (!found) {
            return null;
        }
        
        return ctx.construireChemin(src, nearestCandidate.get(), robot);
    }

    /**
     * Effectue l'étape principale de recherche de chemin de Dijkstra dans le contexte donné.
     *
     * @param selector          Fonction pour filtrer les cases qui sont qualifiées comme destination.
     *                          Doit retourner true si et seulement si la case est acceptée comme destination candidate.
     * @param destinationFoundCallback Callback appelé lorsqu'une destination candidate est trouvée.
     *                          Doit retourner true pour continuer la recherche et false pour l'arrêter.
     * @return true si au moins un chemin a été trouvé, false sinon
     */
    private boolean runSearch(DijkstraContexte ctx, Robot robot, Function<Case, Boolean> selector,
            Function<Case, Boolean> destinationFoundCallback) {
        boolean found = false;

        while (!ctx.queue.isEmpty()) {
            Case u = ctx.queue.poll();

            // Path found
            if (selector.apply(u) && robot.getVitesseDeplacement(u.getTerrain()) > 0) {
                found = true;
                boolean continue_ = destinationFoundCallback.apply(u);
                if (!continue_) {
                    return true;
                }
            }

            for (Case v : carte.iterVoisins(u)) {
                if (!ctx.queue.contains(v)) {
                    // Noeud déjà visité
                    continue;
                }

                double speed = robot.getVitesseDeplacement(v.getTerrain());
                if (speed == 0) {
                    // Le robot ne peut pas traverser cette case
                    continue;
                }

                double alt = ctx.getDistance(u) + robot.tempsDeDeplacement(u, v, carte);
                if (alt < ctx.getDistance(v)) {
                    ctx.setPrecedent(v, u);
                    ctx.setDistance(v, alt);
                    // Replace v au bon endroit dans la queue
                    ctx.queue.remove(v);
                    ctx.queue.add(v);
                }
            }
        }

        return found;
    }
}
