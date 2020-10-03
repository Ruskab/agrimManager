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

public class VehicleGateway extends RestGateway implements Serializable {

    private static final String API_PATH = "app.api.base.path";
    private static final String APP_BASE_URL = "app.url";
    private static final String VEHICLES = "api.vehicles.path";
    private final String authToken;
    private final String resource;

    public VehicleGateway(String authToken) {
        super();
        this.authToken = authToken;
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(VEHICLES);
    }

    public String create(Vehicle vehicleDto) {
        Response response = client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.CREATED);
        return response.readEntity(String.class);
    }

    public List<Vehicle> listAll() {
        return client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<>() {
                });
    }

    public Vehicle read(String vehicleId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + vehicleId))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(Vehicle.class);

    }

    public List<Vehicle> searchBy(String query) {
        return client.target(UriBuilder.fromPath(resource).queryParam("query", query))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<>() {
                });
    }

    public List<Vehicle> searchBy(Client client) {
        return this.client.target(UriBuilder.fromPath(resource).queryParam("clientId", client.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<>() {
                });
    }

    public void update(Vehicle vehicleDto) {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + vehicleDto.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
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
            throw new IllegalStateException(String.format("Client API invalid status: %s", response.getStatus()));
        }
    }
}
