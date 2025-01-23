/*
 * Path.java

 */

package pathfinding;

import donnees.carte.Case;
import donnees.robots.Robot;
import evenements.Evenement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Optional;

public class Path {
    private final LinkedList<Case> cases;
    private final LinkedList<Double> durees;
    private double dureeTotal;
    private final Robot robot;

    /**
     * Construit un chemin vide
     */
    public Path(Robot robot) {
        this.cases = new LinkedList<>();
        this.durees = new LinkedList<>();
        this.robot = robot;
    }

    /**
     * Renvoie la première case du chemin si elle existe
     */
    public Optional<Case> getDebut() {
        return Optional.ofNullable(this.cases.getFirst());
    }

    /**
     * Renvoie la dernière case du chemin si elle existe
     */
    public Optional<Case> getFin() {
        return Optional.ofNullable(this.cases.getLast());
    }

    /**
     * Insère une étape au début du chemin
     *
     * @param case_ Case à ajouter
     */
    public void ajouterDebut(Case case_, double duration) {
        this.cases.addFirst(case_);
        this.durees.addFirst(duration);
        this.dureeTotal += duration;
    }

    /**
     * Retourne les cases qui composent le chemin, en partant du départ
     */
    public Iterable<Case> getCases() {
        return this.cases;
    }

    /**
     * Retourne les étapes qui composent le chemin, en partant du départ
     */
    public Iterable<PathStep> getSteps() {
        ArrayList<PathStep> steps = new ArrayList<>(Math.max(this.cases.size() - 1, 0));
        Iterator<Case> it = this.cases.iterator();
        
        if (!it.hasNext()) {
            return steps;
        }
        
        Case from = it.next();

        while (it.hasNext()) {
            Case to = it.next();
            steps.add(new PathStep(from, to));
            from = to;
        }

        return steps;
    }

    /**
     * Retourne les étapes qui composent le chemin ainsi que leurs durées, en partant du départ
     */
    public Iterable<TimedPathStep> getTimedSteps() {
        ArrayList<TimedPathStep> steps = new ArrayList<>(Math.max(this.cases.size() - 1, 0));
        Iterator<Case> caseIterator = this.cases.iterator();
        Iterator<Double> durationIterator = this.durees.iterator();

        if (!caseIterator.hasNext() || !durationIterator.hasNext()) {
            return steps;
        }

        Case from = caseIterator.next();

        while (caseIterator.hasNext()) {
            Case to = caseIterator.next();
            double duration = durationIterator.next();
            steps.add(new TimedPathStep(from, to, duration));
            from = to;
        }

        return steps;
    }

    /**
     * Retourne la durée totale du chemin
     * 
     * @return
     */
    public double getDureeTotal() {
        return dureeTotal;
    }

    /**
     * Convertit ce chemin en une liste d'événements de déplacement
     * 
     * @param baseDate
     * @return
     */
    public Iterable<Evenement> toEvenements(double baseDate) {
        ArrayList<Evenement> events = new ArrayList<>(Math.max(0, this.cases.size() - 1));
        for (TimedPathStep step: getTimedSteps()) {
            Evenement event = step.conversionEvenement(baseDate, robot);
            events.add(event);
            baseDate = event.getDate();
        }
        return events;
    }
}