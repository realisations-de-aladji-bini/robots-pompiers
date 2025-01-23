/*
 * DeplacementCase.java

 */

package evenements;

import donnees.carte.Case;
import donnees.robots.Robot;

public class DeplacementCase extends Evenement {
    private Robot robot;
    private Case destination;

    public DeplacementCase(double date, Robot robot, Case destination) {
        super(date);

        this.robot = robot;
        this.destination = destination;
    }

    @Override
    public void execute() {
        robot.setPosition(destination);
        robot.avancer();
    }

    @Override
    public String toString() {
        return "Le robot " + robot + " se déplace vers la case " + destination;
    }
}
