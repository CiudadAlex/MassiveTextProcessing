package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AppLogger.
 *
 * @author alciucam
 *
 */
public final class AppLogger {

    private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

    private AppLogger() {
    }

    /**
     * Log debug.
     *
     * @param message
     *            message
     */
    public static synchronized void logDebug(final String message) {
        LOGGER.info(message);
    }

    /**
     * Log error.
     *
     * @param message
     *            message
     */
    public static synchronized void logError(final String message) {
        LOGGER.severe(message);
    }

    /**
     * Log error.
     *
     * @param message
     *            message
     * @param t
     *            Throwable
     */
    public static synchronized void logError(final String message, final Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }

    /**
     * Log error not very verbose.
     *
     * @param message
     *            message
     * @param t
     *            Throwable
     */
    public static synchronized void logErrorNotVerbose(final String message, final Throwable t) {
        LOGGER.log(Level.SEVERE, message + " >> " + t);
    }
}
