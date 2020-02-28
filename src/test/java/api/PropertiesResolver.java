package api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesResolver {

    public Properties loadPropertiesFile(String filePath) {
        Properties properties = new Properties();
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file : " + filePath);
        }
        return properties;
    }

}
