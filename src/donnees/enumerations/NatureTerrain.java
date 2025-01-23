/*
 * NatureTerrain.java

 */

package donnees.enumerations;

public enum NatureTerrain {
    EAU("./assets/nature/eau.jpg"),
    FORET("./assets/nature/foret.jpg"),
    ROCHE("./assets/nature/roche.jpg"),
    TERRAIN_LIBRE("./assets/nature/terrain_libre.jpg"),
    HABITAT("./assets/nature/habitat.jpg");

    private String image = "";

    /**
     * Constructeur de NatureTerrain
     * @param image Le chemin de l'image de la nature du terrain
     */

    NatureTerrain(String image){
        this.image = image;
    }

    /**
     * Retourne le chemin associé à l'image de la nature courante
     * @return La chaîne de caractères représentant le chemin
     */
    public String getImage(){
        return this.image;
    }
}
