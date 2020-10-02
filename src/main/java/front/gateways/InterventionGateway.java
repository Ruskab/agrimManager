package front.gateways;

import front.dtos.Intervention;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

public class InterventionGateway extends RestGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String INTERVENTIONS = "api.interventions.path";
    private static final String API_PATH = "app.api.base.path";
    private final String authToken;
    private final String resource;

    public InterventionGateway(String authToken) {
        this.authToken = authToken;
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS);
    }

    public String create(Intervention intervention) {
        Response response = client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(intervention, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.CREATED);
        return response.readEntity(String.class);
    }

    public Intervention read(String interventionId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + interventionId))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(Intervention.class);
    }

    public void update(Intervention intervention) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + intervention.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(intervention, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
    }

    public void delete(Integer id) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + id))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
        checkResponseStatus(response, Response.Status.NO_CONTENT);
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new IllegalStateException(String.format("API invalid status: %s", response.getStatus()));
        }
    }
}
