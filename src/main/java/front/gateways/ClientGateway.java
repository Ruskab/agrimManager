package front.gateways;

import front.dtos.Client;
import front.dtos.Vehicle;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;
import java.util.List;

public class ClientGateway extends RestGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String CLIENTS = "api.clients.path";
    private static final String API_PATH = "app.api.base.path";
    private final String authToken;
    private final String resource;

    public ClientGateway(String authToken) {
        this.authToken = authToken;
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS);
    }

    public String create(Client client) {
        Response response = this.client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(client, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.CREATED);
        return response.readEntity(String.class);
    }

    public List<Client> readAll() {
        return client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Client>>() {
                });
    }

    public Client read(String clientId) {
        return this.client.target(UriBuilder.fromPath(resource).path("/" + clientId))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(front.dtos.Client.class);
    }

    public List<Client> searchBy(String query) {
        return client.target(UriBuilder.fromPath(resource).queryParam("query", query))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Client>>() {
                });
    }

    public void update(Client client) throws IllegalStateException {
        Response response = this.client.target(UriBuilder.fromPath(resource).path("/" + client.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(client, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.OK);
    }

    public void delete(int id) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + id))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
        checkResponseStatus(response, Response.Status.NO_CONTENT);
    }

    private void checkResponseStatus(Response response, Response.Status status) {
        if (response.getStatus() != status.getStatusCode()) {
            throw new IllegalStateException(String.format("API response invalid status: %s", response.getStatus()));
        }
    }

    public List<Vehicle> searchClientVehicles(Client clientModel) {
        return client.target(UriBuilder.fromPath(resource).path(clientModel.getId() + "/vehicles").build())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<Vehicle>>() {
                });

    }
}
