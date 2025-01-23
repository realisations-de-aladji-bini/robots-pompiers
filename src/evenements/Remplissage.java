/*
 * Remplissage.java

 */

package evenements;

import donnees.DonneesSimulation;
import donnees.enumerations.NatureTerrain;
import donnees.enumerations.TypeRobot;
import donnees.robots.Robot;

public class Remplissage extends Evenement {
    private DonneesSimulation donneesSimulation;
    private Robot robot;

    public Remplissage(double date, DonneesSimulation donneesSimulation, Robot robot) {
        super(date);

        this.donneesSimulation = donneesSimulation;
        this.robot = robot;
    }

    @Override
    public void execute() {
        if (this.robot.getTypeRobot() == TypeRobot.DRONE) {
            // Le drone robot ne peut se remplir que s'il est sur une case d'eau
            if (this.donneesSimulation.getCarte()
                    .getCase(this.robot.getPosition().getLigne(), this.robot.getPosition().getColonne())
                    .getTerrain() != NatureTerrain.EAU) {
                throw new IllegalStateException("Le drone (" + this.robot + ") doit être au dessus de l'eau ("
                        + this.donneesSimulation.getCarte().getCase(this.robot.getPosition().getLigne(),
                                this.robot.getPosition().getColonne())
                        + ")");
            }
        }

        else if (this.robot.getTypeRobot() == TypeRobot.ROUES || this.robot.getTypeRobot() == TypeRobot.CHENILLES) {
            int ligne = this.robot.getPosition().getLigne();
            int colonne = this.robot.getPosition().getColonne();
            boolean eauTrouve = false;

            // Les drones roues et chenilles ne peuvent se remplir que s'ils sont à côté
            // d'une case d'eau

            // On s'assure de ne pas sortir de la carte
            int ligneSuperieure = Math.max(ligne - 1, 0);
            int ligneInferieure = Math.min(ligne + 1, this.donneesSimulation.getCarte().getLignes() - 1);
            int colonneGauche = Math.max(colonne - 1, 0);
            int colonneDroite = Math.min(colonne + 1,
                    this.donneesSimulation.getCarte().getColonnes() - 1);

            for (int i = ligneSuperieure; i <= ligneInferieure; i++) {
                for (int j = colonneGauche; j <= colonneDroite; j++) {
                    if (i == ligne && j == colonne) {
                        continue;
                    }

                    if (this.donneesSimulation.getCarte().getCase(i, j).getTerrain() == NatureTerrain.EAU) {
                        eauTrouve = true;
                        break;
                    }
                }
            }

            if (!eauTrouve) {
                throw new IllegalStateException("Le robot (" + this.robot + ") doit être à côté d'une case d'eau");
            }
        }

        this.robot.remplirReservoir();
    }

    @Override
    public String toString() {
        return "Le robot (" + robot + ") se remplit";
    }
}
