package logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Used for logging various events in the REST API wrapper.
 */
public class LoggerUnit {
    private static final Logger logger;


    private LoggerUnit() {}

    static {
        logger = LoggerFactory.getLogger(LoggerUnit.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    }

    public static void logInfo(String text) {
        logger.info("(" + LocalDateTime.now() + ") " + text);
    }

    public static void logError(String text) {
        logger.error("(" + LocalDateTime.now() + ") " + text);
    }

    public static void logWarning(String text) {
        logger.warn("(" + LocalDateTime.now() + ") " + text);
    }
}
