package front.gateways;

import api.dtos.ClientDto;
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

public class ClientGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String CLIENTS = "api.clients.path";
    private static final String API_PATH = "app.api.base.path";
    private final Client client;
    private final String authToken;
    private final String resource;

    public ClientGateway(String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        Properties properties = new PropertyLoader().loadPropertiesFile("config.properties");
        resource = properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS);
        this.authToken = authToken;
    }

    public String create(ClientDto clientDto) {
        Response response = client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        checkResponseStatus(response, Response.Status.CREATED);
        return response.readEntity(String.class);
    }

    public List<ClientDto> readAll() {
        return client.target(UriBuilder.fromPath(resource))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<ClientDto>>() {
                });
    }

    public ClientDto read(String clientId) {
        return client.target(UriBuilder.fromPath(resource).path("/" + clientId))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(ClientDto.class);
    }

    public List<ClientDto> searchBy(String query) {
        return client.target(UriBuilder.fromPath(resource).queryParam("query", query))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<ClientDto>>() {
                });
    }

    public void update(ClientDto clientDto) throws IllegalStateException {
        Response response = client.target(UriBuilder.fromPath(resource).path("/" + clientDto.getId()))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
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

}
