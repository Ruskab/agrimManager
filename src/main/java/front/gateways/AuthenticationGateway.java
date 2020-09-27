package front.gateways;

import api.dtos.CredentialsDto;
import api.exceptions.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import front.util.PropertyLoader;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class AuthenticationGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String AUTHENTICATION = "api.authentication.path";
    private static final String API_PATH = "app.api.base.path";
    private final Client client;
    private final String resource;

    public AuthenticationGateway() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        Properties properties = new PropertyLoader().loadPropertiesFile("config.properties");
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(AUTHENTICATION);
    }

    public String authenticate(CredentialsDto credentialsDto) throws UnauthorizedException {
        Response response = client.target(UriBuilder.fromPath(resource))
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
