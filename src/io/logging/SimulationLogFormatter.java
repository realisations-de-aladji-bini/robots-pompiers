package io.logging;

import simulations.Simulateur;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimulationLogFormatter extends Formatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final Simulateur simulateur;

    public SimulationLogFormatter(Simulateur simulateur) {
        this.simulateur = simulateur;
    }

    public static String formatSimulationDate(long millis) {
        long seconds = millis / 1000;
        millis %= 1000;

        long minutes = seconds / 60;
        seconds %= 60;

        long hours = minutes / 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    @Override
    public String format(LogRecord record) {
        return String.format("%s %s %s %s\n",
                ANSI_GREEN + record.getLevel() + ANSI_RESET,
                ANSI_PURPLE + formatSimulationDate((long)(simulateur.getDateSimulation() * 1000)) + ANSI_RESET,
                ANSI_BLUE + record.getLoggerName() + ANSI_RESET,
                record.getMessage()
        );
    }
}
