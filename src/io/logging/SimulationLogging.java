package io.logging;

import simulations.Simulateur;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class SimulationLogging {
    /**
     * Modifie le logger racine pour customiser l'affichage des logs, afin d'afficher le temps interne à la simulation.
     * Cette méthode ne doit être appelée qu'une seule fois et ne fonctionnera qu'avec un seul simulateur.
     * @param simulateur Le simulateur pour lequel on compte afficher des logs.
     */
    public static void setup(Simulateur simulateur) {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimulationLogFormatter(simulateur));

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setUseParentHandlers(false);
        // Remove default handlers
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        rootLogger.addHandler(handler);
    }
}
