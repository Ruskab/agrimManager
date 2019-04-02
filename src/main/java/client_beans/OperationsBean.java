package client_beans;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;


@ManagedBean
@ViewScoped
public class OperationsBean {

    Client client;
    Properties properties;


    @PostConstruct
    public void init(){
        // Create an ObjectMapper to be used for (de)serializing to/from JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule for JSR-310 DateTime (de)serialization
        objectMapper.registerModule(new JavaTimeModule());
        // Configure the object mapper te serialize to timestamp strings.
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Create a Jackson Provider
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = this.loadPropertiesFile("config.properties");
    }

    public void generateFakeData() {
        List<Integer> clientsIds = new ArrayList<>();
        List<Integer> vehiclesIds = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            ClientDto clientDto = new ClientDto("fake Client " + i, new Random().ints(0, 40).findFirst().getAsInt());
            clientsIds.add(addFakeClient(clientDto)) ;
        }
        addMessage("Success", "Added new 30 clients");
        for (int i = 0; i < 60; i++) {

            Collections.shuffle(clientsIds);
            VehicleDto vehicleDto = createFakeVehicleDto(clientsIds.get(0).toString());
            vehiclesIds.add(addFakeVehicle(vehicleDto));
        }
        addMessage("Success", "Added new 60 vehicles");
    }

    private Integer addFakeVehicle(VehicleDto vehicleDto ) {

        Response response = client.target(properties.getProperty("app.url")+"/api/vehicles")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(Integer.class);
    }

    private VehicleDto createFakeVehicleDto(String clientId) {
        List<String> brands = Arrays.asList("Piaggio", "Suzuki", "Lexus", "Jaguar", "Iveco", "Lancia", "Volkswagen", "Bentley", "Tata", "BMW",
                "Seat", "Nissan", "Piaggio", "Suzuki", "Lexus", "Jaguar", "Iveco", "Lancia", "Volkswagen", "Bentley", "Tata",
                "BMW", "Seat", "Nissan", "Isuzu", "Aston Martin", "Smart", "Ferrari", "Citroen", "Skoda", "Opel", "Peugeot",
                "Kia", "Alfa Romeo", "Tesla", "Cadillac", "Mercedes-Benz", "SsangYong", "Lamborghini", "Jeep", "Audi",
                "Mitsubishi", "Land Rover", "Volvo", "Honda", "Ford", "Toyota", "Infiniti", "Chevrolet", "Renault", "Lotus",
                "Fiat", "Abarth", "Dacia", "Maserati", "Mazda", "Caterham", "Subaru", "Rolls-Royce", "Mini", "Lada", "Porsche", "KTM", "Morgan");

        Collections.shuffle(brands);

        return new VehicleDtoBuilder()
                .setRegistrationPlate(getSaltString())
                .setClientId(clientId)
                .setBrand(brands.get(0))
                .setKMS("03-03-2017 94744")
                .setBodyOnFrame("VF1KC0JEF"+new Random().ints(1, 5000).findFirst().getAsInt()+"32")
                .setLastRevisionDate(LocalDate.now().minusMonths(new Random().ints(1, 5).findFirst().getAsInt()))
                .setItvDate(LocalDate.now().minusMonths(new Random().ints(1, 5).findFirst().getAsInt()))
                .setNextItvDate(LocalDate.now().plusYears(new Random().ints(1, 5).findFirst().getAsInt()))
                .setAirFilterReference(new Random().ints(1000, 100000000).findFirst().getAsInt() + "3")
                .setOilFilterReference(new Random().ints(1000, 100000000).findFirst().getAsInt() +"1")
                .setFuelFilter(new Random().ints(1000, 100000000).findFirst().getAsInt() + "2")
                .setMotorOil("5.5  5W30")
                .createVehicleDto();
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private Integer addFakeClient(ClientDto clientDto) {

        Response response = client.target(new Properties().getProperty("app.url")+"/api/clients")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(Integer.class);
    }

    public void destroyTheWorld(){
        deleteAllVehicles();
        addMessage("Success", "All vehicles deleted");
        deleteAllClients();
        addMessage("Success", "All clients deleted");
    }

    private void deleteAllVehicles() {
        List<VehicleDto> vehicleDtoList = client.target(properties.getProperty("app.url")+"/api/vehicles/")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<VehicleDto>>() {});
        vehicleDtoList.forEach(vehicleDto -> client.target(properties.getProperty("app.url")+"/api/vehicles/" + vehicleDto.getId())
                .request(MediaType.APPLICATION_JSON).delete());
    }

    private void deleteAllClients() {
        List<ClientDto> clientDtoLIst = client.target(properties.getProperty("app.url")+"/agrimManager_war_exploded/api/clients/")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<ClientDto>>() {});
        clientDtoLIst.forEach(clientDto -> client.target(properties.getProperty("app.url")+"/api/clients/" + clientDto.getId())
                .request(MediaType.APPLICATION_JSON).delete());
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private Properties loadPropertiesFile(String filePath) {

        Properties prop = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file : " + filePath);
        }
        return prop;
    }
}

