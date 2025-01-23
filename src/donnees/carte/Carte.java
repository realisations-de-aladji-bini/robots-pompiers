/*
 * Carte.java

 */

package donnees.carte;

import donnees.enumerations.Direction;
import exceptions.VoisinInexistant;

import java.util.Arrays;

public class Carte {
    private int nbLignes;
    private int nbColonnes;
    private Case[][] cases;
    private int tailleCase;

    /**
     * Constructeur de la classe Carte
     * @param lignes Le nombre de lignes de la carte
     * @param colonnes Le nombre de colonnes de la carte
     * @param cases La matrice de cases
     * @param taille La taille d'une case en mètres
     */
    public Carte(int lignes, int colonnes, Case[][] cases, int taille) {
        this.nbLignes = lignes;
        this.nbColonnes = colonnes;
        this.cases = cases;
        this.tailleCase = taille;
    }

    /**
     * Renvoie le nombre de lignes de la carte
     */
    public int getLignes(){
        return this.nbLignes;
    }

    /**
     * Renvoie le nombre de colonnes de la carte
     * @return Le nombre de colonnes de la carte courante
     */
    public int getColonnes(){
        return this.nbColonnes;
    }

    /**
     * Retourne la taille d'une case de la carte
     * @return La taille d'une case de la carte
     */
    public int getTailleCase() {
        return this.tailleCase;
    }

    /**
     * Une case particulière de la carte
     * @param ligne La ligne de la case
     * @param colonne La colonne de la case
     * @return La case
     */
    public Case getCase(int ligne, int colonne){
        return cases[ligne][colonne];
    }

    /**
     * Regarde si une case donnée a un voisin dans une certaine direction
     * @param src Case de départ
     * @param dir Direction dans laquelle rechercher un voisin
     * @return true si le voisin existe sinon false
     */
    public boolean voisinExiste(Case src, Direction dir) {
        int lig = src.getLigne();
        int col = src.getColonne();
        switch (dir) {
            case NORD -> {
                lig--;
            }
            case SUD -> {
                lig++;
            }
            case EST -> {
                col++;
            }
            case OUEST -> {
                col--;
            }
            case STATIONNAIRE -> {
            }
        }
        return (0 <= lig && lig < this.getLignes()) && (0 <= col && col < this.getColonnes());
    }

    /**
     * Retourne le voisin d'une case dans une direction donnée
     * @param src Case de départ
     * @param dir Direction vers le voisin
     * @return Le voisin s'il existe
     */
    public Case getVoisin(Case src, Direction dir) throws VoisinInexistant {
        if (!this.voisinExiste(src, dir)) {
            throw new VoisinInexistant("le voisin de la case " + src + " dans la direction " + dir + " n'existe pas");
        }
        switch (dir) {
            case NORD -> {
                return this.getCase(src.getLigne() - 1, src.getColonne());
            }
            case SUD -> {
                return this.getCase(src.getLigne() + 1, src.getColonne());
            }
            case EST -> {
                return this.getCase(src.getLigne(), src.getColonne() + 1);
            }
            case OUEST -> {
                return this.getCase(src.getLigne(), src.getColonne() - 1);
            }
            case STATIONNAIRE -> {
                return src;
            }
        }
        return src; // for static analysis
    }

    /**
     * Itérateur sur tous les voisins directs d'une case (au plus 4)
     *
     * @param src Case de départ
     * @return Les voisins existants de la case donnée
     */
    public Iterable<Case> iterVoisins(Case src) {
        Direction[] dirs = {Direction.NORD, Direction.SUD, Direction.EST, Direction.OUEST};
        return Arrays.stream(dirs)
                .filter(dir -> this.voisinExiste(src, dir))
                .map(dir -> this.getVoisin(src, dir))
                .toList();
    }
}
