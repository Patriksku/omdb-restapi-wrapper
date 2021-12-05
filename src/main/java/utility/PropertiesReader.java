package utility;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import logging.LoggerUnit;

/**
 * Responsible for retrieving the API key for OMDb API in a safe manner.
 */
public class PropertiesReader {

    private static final Properties PROPERTIES;
    private static final String PROP_FILE = "keys.properties";

    private PropertiesReader() {}

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROP_FILE);
        try {
            PROPERTIES.load(props.openStream());
        } catch (IOException ex) {
            System.out.println(ex);
            LoggerUnit.logError("Error occurred on reading the OMDb API key.");
        }
    }

    /**
     * @return Return API key for OMDb.
     */
    public static String getAPIKey() {
            return PROPERTIES.getProperty("OMDB_APIKEY");
    }


}
