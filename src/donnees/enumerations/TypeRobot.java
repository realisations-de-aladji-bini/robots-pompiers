/*
 * TypeRobot.java

 */

package donnees.enumerations;

/**
 * Les différents de robots
 */
public enum TypeRobot {
    CHENILLES,
    DRONE,
    PATTES,
    ROUES;

    @Override
    public String toString() {
        return switch (this) {
            case CHENILLES -> "Chenilles";
            case DRONE -> "Drone";
            case PATTES -> "Pattes";
            case ROUES -> "Roues";
        };
    }
}
