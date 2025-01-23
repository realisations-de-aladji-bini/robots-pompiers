/*
 * Carte.java

 */

package donnees.carte;

import donnees.enumerations.NatureTerrain;

/**
 * Classe répresentant une case carrée
 */
public class Case implements Cloneable {
    private int ligne;
    private int colonne;
    private NatureTerrain terrain;

    /**
     * Constructeur de la classe Case
     * @param ligne Ligne de la case
     * @param colonne Colonne de la case
     * @param terrain Nature du terrain sur lequel se trouve cette case
     */
    public Case(int ligne, int colonne, NatureTerrain terrain) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.terrain = terrain;
    }

    /**
     * Retourne l'indice de la ligne à sur laquelle se trouve la case
     * @return Indice de la ligne de la case
     */
    public int getLigne() {
        return this.ligne;
    }

    /**
     * Retourne la colonne de la case courante
     * @return Colonne de la case
     */
    public int getColonne(){
        return this.colonne;
    }

    /**
     * Renvoie la nature du terrain de la case
     * @return La nature du terrain de la case courante
     */
    public NatureTerrain getTerrain(){
        return this.terrain;
    }

    /**
     * Met à jour la nature du terrain de la case courante
     * @param terrain Le nouveau terrain
     */
    public void setTerrain(NatureTerrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Affiche une casse
     * @return La chaîne de caractères affichant la case
     */
    @Override
    public String toString() {
        return "(" + this.ligne + ", " + this.colonne + " (" + this.terrain + "))";
    }
}
