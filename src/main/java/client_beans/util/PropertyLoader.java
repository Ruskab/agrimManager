package client_beans.util;

import client_beans.mechanics.MechanicCreateBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private static final Logger LOGGER = LogManager.getLogger(PropertyLoader.class);

    public Properties loadPropertiesFile(String filePath) {

        Properties prop = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            LOGGER.error("Unable to load properties file : {}" , filePath);
        }
        return prop;
    }

    public boolean isProduction() {
        return this.loadPropertiesFile("config.properties").getProperty("api.environment").equals("production");
    }
}
