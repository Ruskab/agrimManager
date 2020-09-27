package front.gateways;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
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

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class VehicleGateway implements Serializable {

    private static final String API_PATH = "app.api.base.path";
    private static final String APP_BASE_URL = "app.url";
    private static final String VEHICLES = "api.vehicles.path";
    private final Client client;
    private final String authToken;
    private final String resource;

    public VehicleGateway(String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        Properties properties = new PropertyLoader().loadPropertiesFile("config.properties");
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(VEHICLES);
        this.authToken = authToken;
    }

    public String create(VehicleDto vehicleDto) {
        Response response = client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.CREATED);
        return response.readEntity(String.class);
    }

    public List<VehicleDto> readAll() {
        return client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<VehicleDto>>() {});
    }

    public VehicleDto read(String vehicleId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + vehicleId))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(VehicleDto.class);

    }

    public List<VehicleDto> searchBy(String query) {
        return client.target(UriBuilder.fromPath(resource).queryParam("query", query))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<VehicleDto>>() {});
    }

    public List<VehicleDto> searchBy(ClientDto clientDto) {
        return client.target(UriBuilder.fromPath(resource).queryParam("clientId", clientDto.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<VehicleDto>>() {});
    }

    public void update(VehicleDto vehicleDto) {
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
