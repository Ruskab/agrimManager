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
import java.util.List;
import java.util.Properties;

import static javax.ws.rs.client.Entity.entity;
import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class MechanicGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String MECHANICS = "api.mechanics.path";
    private static final String API_PATH = "app.api.base.path";
    private final Client client;
    private final String authToken;
    private final String resource;

    public MechanicGateway(String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        Properties properties = new PropertyLoader().loadPropertiesFile("config.properties");
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS);

        this.authToken = authToken;
    }

    public String create(MechanicDto mechanicDto) {
        Response response = client.target(UriBuilder.fromPath(resource).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(String.class);
    }

    public void createIntervention(MechanicDto mechanic, InterventionDto interventionDto) {
        client.target(UriBuilder.fromPath(resource).path(mechanic.getId() + "/interventions").build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
    }

    public void finishIntervention(MechanicDto mechanicDto, InterventionDto interventionDto) {
        Response response = client.target(UriBuilder.fromPath(resource).path(mechanicDto.getId() + "/interventions/" + interventionDto.getId() + "/finish").build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
    }

    public List<MechanicDto> readAll() {
        return client.target(UriBuilder.fromPath(resource).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<MechanicDto>>() {
                });
    }

    public MechanicDto read(String mechanicId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + mechanicId).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(MechanicDto.class);
    }

    public List<MechanicDto> searchByCredentials(String username) {
        return client.target(UriBuilder.fromPath(resource).queryParam("username", username).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<MechanicDto>>() {
                });
    }

    public List<InterventionDto> searchInterventions(String mechanicId, Boolean active) {
        return client.target(UriBuilder.fromPath(resource).path(mechanicId + "/interventions").queryParam("active", active.toString()).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<InterventionDto>>() {
                });
    }

    public Integer update(MechanicDto mechanicDto) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + mechanicDto.getId()).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.getStatus();
    }

    public void delete(int id) {
        client.target(UriBuilder.fromPath(resource).path("/" + id).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new IllegalStateException(String.format("API invalid status: %s", response.getStatus()));
        }
    }
}
