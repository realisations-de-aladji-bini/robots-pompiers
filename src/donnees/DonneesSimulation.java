/*
 * DonneesSimulation.java

 */

package donnees;

 import java.util.LinkedList;
import donnees.carte.Carte;
import donnees.carte.Case;
import donnees.incendie.Incendie;
import donnees.robots.Robot;

public class DonneesSimulation {
    private Carte carte;
    private LinkedList<Incendie> incendies;
    private LinkedList<Robot> robots;

    /**
     * Constructeur de la classe DonneesSimulation
     * @param carte La carte de simulation
     * @param incendies Liste des incendies présents sur la carte
     * @param robots Liste des robots présents sur la carte
     */
    public DonneesSimulation(Carte carte, LinkedList<Incendie> incendies, LinkedList<Robot> robots) {
        this.carte = carte;
        this.incendies = incendies;
        this.robots = robots;
    }

    /**
     * Retourne la carte contenant les données de simulation
     * @return La carte de simulation
     */
    public Carte getCarte(){
        return this.carte;
    }

    /**
     * Retourne la liste des robots pour la simulation
     * @return La liste des robots pour la simulation
     */
    public LinkedList<Robot> getRobots() {
        return this.robots;
    }

    /**
     * Retourne un robot en particulier
     * 
     * @param i L'indice du robot à retourner
     * 
     * @return Le robot à l'indice i
     */
    public Robot getRobot(int i) {
        return this.robots.get(i);
    }

    /**
     * Renvoie la liste des incendies présents pour la simulation
     * @return La liste des incendies
     */
    public LinkedList<Incendie> getIncendies() {
        return this.incendies;
    }

    /**
     * Renvoie un incendie en particulier
     * 
     * @param i L'indice de l'incendie à retourner
     * 
     * @return L'incendie à l'indice i
     */
    public Incendie getIncendie(int i) {
        return this.incendies.get(i);
    }

    /**
     * Renvoie l'incendie sur la case donnée s'il existe
     * 
     * @param position La case où l'on veut chercher un incendie
     * 
     * @return L'incendie sur la case donnée s'il est trouvé, null sinon
     */
    public Incendie getIncendieSurCase(Case position) {
        for (Incendie i : this.getIncendies()) {
            if (i.getPosition().equals(position)) {
                return i;
            }
        }

        return null;
    }

    /**
     * Donner le plus grand volume d'eau nécessaire pour éteindre un incendie
     * @return Le volume d'eau nécessaire
     */
    public int getVolumeEauNecessaire(){
        int vMax = 0;
        for(Incendie i: getIncendies()){
            int volumeEauIncendie = i.getEauNecessaire();
            vMax = Math.max(volumeEauIncendie, vMax);
        }

        return vMax;
    }

    public LinkedList<Incendie> clonerIncendies(LinkedList<Incendie> incendies){
        LinkedList<Incendie> copieIncendies = new LinkedList<>();
        for(Incendie i: incendies) {
            copieIncendies.add((Incendie)i.clone());
        }

        return copieIncendies;
    }

    /**
     * Crée une copie des robots
     * @param robots Une liste de robots à cloner
     * @return Les robots clonés
     */
    public LinkedList<Robot> clonerRobots(LinkedList<Robot> robots){
        LinkedList<Robot> copieRobots = new LinkedList<>();
        for(Robot r: robots){
            copieRobots.add((Robot) r.clone());
        }

        return copieRobots;
    }

    /**
     * Clone les données de simulations
     * @return Une copie des données de simulation
     */

    public DonneesSimulation clonerDonnees() {
        LinkedList<Incendie> copieIncendies = this.clonerIncendies(this.incendies);
        LinkedList<Robot> copieRobots = this.clonerRobots(this.robots);

        return new DonneesSimulation(carte, copieIncendies, copieRobots);
    }

    /**
     * Recopie les propriétés des données de la simulation dans this. Similaire à
     * clonerDonnees mais permet de garder les mêmes références aux objets
     * 
     * @param donnees Les données à recopier
     */
    public void recopierProprietesDonnees(DonneesSimulation donnees) {
        for (int i = 0; i < donnees.getRobots().size(); i++) {
            this.getRobot(i).recopierProprietes(donnees.getRobot(i));
        }

        for (int i = 0; i < donnees.getIncendies().size(); i++) {
            this.getIncendie(i).recopierProprietes(donnees.getIncendie(i));
        }
    }

    /**
     * Méthode d'affichage des données de simulation
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String nLigne = "\n \t";

        sb.append("Données de simulation : ").append(nLigne)
        .append("Carte : ").append(this.getCarte()).append(nLigne)
        .append("Robots : ").append(this.getRobots()).append(nLigne)
        .append("Incendies : ").append(this.getIncendies());

        return sb.toString();
    }
}
