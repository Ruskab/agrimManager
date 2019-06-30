package client_beans.interventions;

import api.dtos.InterventionDto;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class InterventionGateway {

    Client client;
    Properties properties;
    private static final String API_PATH = "app.api.base.path";
    public static final String APP_BASE_URL = "app.url";
    public static final String INTERVENTIONS = "api.interventions.path";

    private static final Logger LOGGER = LogManager.getLogger(InterventionGateway.class);

    public InterventionGateway() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
    }

    public String create(InterventionDto interventionDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
        LOGGER.info("API Crear intervention {} : Status: {}", interventionDto.getId(), response.getStatus());
        return response.readEntity(String.class);
    }

    public Integer update(InterventionDto interventionDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS) + "/" + interventionDto.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
        LOGGER.info("API Actualizar intervention {} : Status: {}", interventionDto.getId(), response.getStatus());
        return response.getStatus();
    }

    public List<InterventionDto> readAll() {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS))
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<InterventionDto>>() {
                });
    }

    public InterventionDto read(String interventionId) {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS) + "/" + interventionId)
                .request(MediaType.APPLICATION_JSON)
                .get(InterventionDto.class);
    }

    public void delete(int id) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(INTERVENTIONS) + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }
}
