package front.gateways;

import front.dtos.Intervention;
import front.dtos.Mechanic;
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

public class MechanicGateway extends RestGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String MECHANICS = "api.mechanics.path";
    private static final String API_PATH = "app.api.base.path";
    private final String authToken;
    private final String resource;

    public MechanicGateway(String authToken) {
        this.authToken = authToken;
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS);
    }

    public String create(Mechanic mechanic) {
        Response response = client.target(UriBuilder.fromPath(resource).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(entity(mechanic, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(String.class);
    }

    public void createIntervention(Mechanic mechanic, Intervention intervention) {
        client.target(UriBuilder.fromPath(resource).path(mechanic.getId() + "/interventions").build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(intervention, MediaType.APPLICATION_JSON_TYPE));
    }

    public void finishIntervention(Mechanic mechanic, Intervention intervention) {
        Response response = client.target(UriBuilder.fromPath(resource).path(mechanic.getId() + "/interventions/" + intervention.getId() + "/finish").build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(entity(intervention, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
    }

    public List<Mechanic> readAll() {
        return client.target(UriBuilder.fromPath(resource).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Mechanic>>() {
                });
    }

    public Mechanic read(String mechanicId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + mechanicId).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(Mechanic.class);
    }

    public List<Mechanic> searchByName(String username) {
        return client.target(UriBuilder.fromPath(resource).queryParam("username", username).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Mechanic>>() {
                });
    }

    public List<Intervention> searchInterventionsByFilter(String mechanicId, Boolean active) {
        return client.target(UriBuilder.fromPath(resource).path(mechanicId + "/interventions").queryParam("active", active.toString()).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Intervention>>() {
                });
    }

    public Integer update(Mechanic mechanic) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + mechanic.getId()).build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(entity(mechanic, MediaType.APPLICATION_JSON_TYPE));
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
