package client_beans.clients;

import api.dtos.ClientDto;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class ClientGateway implements Serializable {

    public static final String APP_BASE_URL = "app.url";
    public static final String CLIENTS = "api.clients.path";
    private static final String API_PATH = "app.api.base.path";
    private static final Logger LOGGER = LogManager.getLogger(ClientGateway.class);
    private Client client;
    private Properties properties;
    private String authToken;

    public ClientGateway(String authToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
        this.authToken = authToken;
    }

    public String create(ClientDto clientDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        LOGGER.info("API Crear cliente {} : Status: {}", clientDto.getFullName(), response.getStatus());
        return response.readEntity(String.class);
    }

    public Integer update(ClientDto clientDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS) + "/" + clientDto.getId())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        LOGGER.info("API Actualizar cliente {} : Status: {}", clientDto.getId(), response.getStatus());
        return response.getStatus();
    }

    public List<ClientDto> readAll() {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(new GenericType<List<ClientDto>>() {
                });
    }

    public ClientDto read(String clientId) {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS) + "/" + clientId)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(ClientDto.class);
    }

    public void delete(int id) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(CLIENTS) + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }
}
