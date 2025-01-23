/*
 * Incendie.java
 */

package donnees.incendie;

import donnees.carte.Case;

public class Incendie implements Cloneable{
    private final String image = "./assets/incendie.png";
    private Case position;
    private int eauNecessaire;

    /**
     * Constructeur de l'incendie
     * @param position La case sur laquelle se trouve l'incendie
     * @param quantiteEau La quantité d'eau nécessaire pour éteindre l'incendie
     */
    public Incendie(Case position, int quantiteEau) {
        this.position = position;
        this.eauNecessaire = quantiteEau;
    }

    /**
     * Récupère la position de l'incendie
     * @return La case sur laquelle se trouve l'incendie
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * Deverse la quantite d'eau donnée sur l'incendie
     * 
     * @param quantiteEau La quantité d'eau à déverser
     */
    public void eteindre(int quantiteEau) {
        eauNecessaire = Math.max(this.eauNecessaire - quantiteEau, 0);
    }

    /**
     * La quantité d'eau pour éteindre l'incendie
     * @return la quantité d'eau necessaire pour éteindre l'incendie
     */
    public int  getEauNecessaire(){
        return this.eauNecessaire;
    }

    /**
     * Image de l'incendie
     * @return Le chemin de l'image de l'incendie
     */
    public String getImage() {
        return image;
    }

    /**
     * Vérifie si l'incendie est éteint
     * @return Un booléen indiquant si oui ou non l'incendie est éteint
     */
    public boolean estEteint(){
        return eauNecessaire==0;
    }

    /**
     * Affichage de l'incendie
     * @return La chaîne représentant l'incendie
     */
    @Override
    public String toString(){
        return "(" + this.getPosition() + "), " + eauNecessaire + "L restants";
    }

    /**
     * Clône l'incendie sur lequel la fonction est appelée
     */
    public Object clone() {
        try {
            Incendie incendie = (Incendie) super.clone();
            incendie.position = this.getPosition();

            return incendie;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recopie les propriétés de l'incendie passé en paramètre dans this
     * Permet de cloner un incendie mais en gardant la référence de l'objet
     * 
     * @param incendie Les propriétés à recopier
     */
    public void recopierProprietes(Incendie incendie) {
        this.position = incendie.getPosition();
        this.eauNecessaire = incendie.getEauNecessaire();
    }
}
