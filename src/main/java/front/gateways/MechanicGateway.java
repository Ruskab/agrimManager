package front.gateways;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import front.util.PropertyLoader;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class MechanicGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String MECHANICS = "api.mechanics.path";
    private static final String API_PATH = "app.api.base.path";
    private static final String MECHANIC_INTERVENTIONS = "api.mechanics.interventions.path";
    private Client client;
    private Properties properties;
    private String authToken;

    public MechanicGateway(String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
        this.authToken = authToken;
    }

    public String create(MechanicDto mechanicDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties
                .getProperty(MECHANICS))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(String.class);
    }

    public Integer update(MechanicDto mechanicDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties
                .getProperty(MECHANICS) + "/" + mechanicDto.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.getStatus();

    }

    public List<MechanicDto> readAll() {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<MechanicDto>>() {
                });
    }

    public MechanicDto read(String mechanicId) {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + mechanicId)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(MechanicDto.class);
    }

    public List<InterventionDto> searchInterventions(String mechanicId, Boolean active) {
        String mechanicResourcePath = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties
                .getProperty(MECHANICS);
        URI endPoint = UriBuilder.fromPath(mechanicResourcePath)
                .path(mechanicId + "/interventions")
                .queryParam(active.toString())
                .build();
        return client.target(endPoint)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<InterventionDto>>() {
                });
    }

    public void finishIntervention(MechanicDto mechanicDto, InterventionDto interventionDto) {
        String mechanicResourcePath = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties
                .getProperty(MECHANICS);
        URI endPoint = UriBuilder.fromPath(mechanicResourcePath)
                .path(mechanicDto.getId() + "/interventions/" + interventionDto.getId() + "/finish")
                .build();

        Response response = client.target(endPoint)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
    }

    public void delete(int id) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

    public void addIntervention(MechanicDto mechanic, InterventionDto interventionDto) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + mechanic
                .getId() + properties.getProperty(MECHANIC_INTERVENTIONS))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new IllegalStateException(String.format("API invalid status: %s", response.getStatus()));
        }
    }
}
