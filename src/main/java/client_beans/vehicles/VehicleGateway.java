package client_beans.vehicles;

import api.api_controllers.VehicleApiController;
import api.dtos.VehicleDto;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;

public class VehicleGateway {

    Client client;
    Properties properties;
    private static final String API_PATH = "/api/v0";
    public static final String APP_BASE_URL = "app.url";
    public static final String VEHICLES = "/vehicles";

    private static final Logger LOGGER = LogManager.getLogger(VehicleGateway.class);

    public VehicleGateway() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertyLoader().loadPropertiesFile("config.properties");
    }

    public String create(VehicleDto vehicleDto){
        Response response = client.target(properties.getProperty("app.url") + API_PATH + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(String.class);
    }

    public Integer update(VehicleDto vehicleDto){
        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + VEHICLES + "/" + vehicleDto.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        return response.getStatus();

    }


}
