/*
 * RobotFactory.java

 */

package donnees.robots;

import donnees.carte.Case;
import exceptions.RobotInconnu;
import pathfinding.Pathfinder;

public class RobotFactory {
    /**
     * Design pattern factory pour créer un robot. Permet de s'abstraire de la
     * création.
     * Il suffit d'appeler cette méthode qui avec le bon type et elle renverra le
     * bon type de robot (qui hérite de robot).
     * 
     * @param type     Le type de robot à créer
     * @param position La position du robot
     * @param vite     se La vitesse du robot
     * 
     * @return Le robot créé
     * 
     * @throws IllegalArgumentException
     * @throws RobotInconnu
     */
    public static Robot creeRobot(String type, Case position, double vitesse, Pathfinder pathfinder)
            throws IllegalArgumentException, RobotInconnu {
        Robot robot;

        switch (type) {
            case "DRONE":
                if (vitesse > 150) {
                    throw new IllegalArgumentException("La vitesse d'un drone ne peut pas excéder 150 km/h");
                }

                // Si vitese n'a pas été spécifié, alors vitesse est égal à -1, on utilise donc
                // la valeur par défaut
                if (vitesse < 0) {
                    robot = new Drone(position);
                } else {
                    robot = new Drone(position, vitesse);
                }

                break;

            case "ROUES":
                if (vitesse < 0) {
                    robot = new RobotRoues(position);
                } else {
                    robot = new RobotRoues(position, vitesse);
                }

                break;

            case "CHENILLES":
                if (vitesse > 80) {
                    throw new IllegalArgumentException("La vitesse d'un robot à pattes ne peut pas excéder 80 km/h");
                }

                if (vitesse < 0) {
                    robot = new RobotChenilles(position);
                } else {
                    robot = new RobotChenilles(position, vitesse);
                }

                break;

            case "PATTES":
                robot = new RobotPattes(position, 30);
                break;

            default:
                throw new RobotInconnu("Type de robot inconnu");
        }

        robot.setPathfinder(pathfinder);

        return robot;
    }
}
