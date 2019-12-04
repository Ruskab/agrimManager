package client_beans.mechanics;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import client_beans.util.PropertyLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

public class MechanicGateway {

    public static final String APP_BASE_URL = "app.url";
    public static final String MECHANICS = "api.mechanics.path";
    private static final String API_PATH = "app.api.base.path";
    private static final String MECHANIC_INTERVENTIONS = "api.mechanics.interventions.path";
    private Client client;
    private Properties properties;

    public MechanicGateway() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
    }

    public String create(MechanicDto mechanicDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(String.class);
    }

    public Integer update(MechanicDto mechanicDto) {
        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + mechanicDto.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(mechanicDto, MediaType.APPLICATION_JSON_TYPE));
        return response.getStatus();

    }

    public List<MechanicDto> readAll() {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS))
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<MechanicDto>>() {
                });
    }

    public MechanicDto read(String mechanicId) {
        return client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + mechanicId)
                .request(MediaType.APPLICATION_JSON)
                .get(MechanicDto.class);

    }

    public void delete(int id) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();
    }

    public void addIntervention(MechanicDto mechanic, InterventionDto interventionDto) {
        client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + properties.getProperty(MECHANICS) + "/" + mechanic.getId() + properties.getProperty(MECHANIC_INTERVENTIONS))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));
    }
}
