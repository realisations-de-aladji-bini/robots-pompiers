/*
 * DecisionChefPompier.java

 */

package evenements;

import chefpompiers.ChefPompier;

public class DecisionChefPompier extends Evenement {
    private ChefPompier chefPompier;

    public DecisionChefPompier(double date, ChefPompier chefPompier) {
        super(date);

        this.chefPompier = chefPompier;
    }

    @Override
    public void execute() {
        this.chefPompier.prendreDecision();
    }

    @Override
    public String toString() {
        return "Le chef pompier prend une décision";
    }
}
