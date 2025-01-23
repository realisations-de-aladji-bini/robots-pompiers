/*
 * DeplacementDirection.java

 */

package evenements;

import donnees.DonneesSimulation;
import donnees.enumerations.Direction;
import donnees.robots.Robot;

public class DeplacementDirection extends Evenement {
    private DonneesSimulation donnees;
    private Robot robot;
    private Direction direction;

    public DeplacementDirection(double date, DonneesSimulation donnees, Robot robot, Direction direction) {
        super(date);

        this.donnees = donnees;
        this.robot = robot;
        this.direction = direction;
    }

    @Override
    public void execute() {
        this.robot.setPosition(this.donnees.getCarte().getVoisin(this.robot.getPosition(), this.direction));
    }

    @Override
    public String toString() {
        return "Le robot " + robot + " se déplace vers la direction " + direction;
    }
}
