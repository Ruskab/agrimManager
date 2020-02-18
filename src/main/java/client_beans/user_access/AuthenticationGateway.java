package client_beans.user_access;

import api.dtos.CredentialsDto;
import api.exceptions.UnauthorizedException;
import client_beans.util.PropertyLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class AuthenticationGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String AUTHENTICATION = "api.authentication.path";
    private static final String API_PATH = "app.api.base.path";
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationGateway.class);
    private Client client;
    private Properties properties;

    public AuthenticationGateway() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
    }

    public String authenticate(CredentialsDto credentialsDto) throws UnauthorizedException {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(AUTHENTICATION))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentialsDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
        return response.readEntity(String.class);
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new UnauthorizedException(String.format("API response invalid status: %s", response.getStatus()));
        }
    }

}
