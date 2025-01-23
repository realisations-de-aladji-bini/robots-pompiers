/*
 * Evenement.java

 */

package evenements;

public abstract class Evenement implements Comparable<Evenement> {
    private double date;

    public Evenement(double date) {
        this.date = date;
    }

    public double getDate() {
        return date;
    }

    public abstract void execute();

    /**
     * Compare deux évènements en fonction de leur date (date la plus petite en
     * premier)
     * Permet d'ordonner facilement les évènements par date (dans une PriorityQueue
     * par exemple)
     * 
     * @param e L'autre évènement à comparer
     * 
     * @return 1 si this a une date plus grande que e, -1 si this a une date plus
     *         petite que e, 0 sinon
     */
    @Override
    public int compareTo(Evenement e) {
        if (this.date > e.date) {
            return 1;
        } else if (this.date < e.date) {
            return -1;
        } else {
            return 0;
        }
    }
}
