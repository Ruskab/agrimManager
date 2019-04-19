package client_beans;

import api.api_controllers.ClientApiController;
import api.api_controllers.VehicleApiController;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

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

    public static final String SUCCESS = "Success";
    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "app.api.base.path";
    private static final Logger LOGGER = LogManager.getLogger(OperationsBean.class);
    Properties properties;
    Client client;
    private DashboardModel model;
    Random rnd = new Random();

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = this.loadPropertiesFile("config.properties");

        initDashboard();

    }

    private void initDashboard() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("createFakeData");
        column2.addWidget("deleteAllData");
        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());

        addMessage(message);
    }

    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");

        addMessage(message);
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());

        addMessage(message);
    }

    public DashboardModel getModel() {
        return model;
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessage(FacesMessage facesMessage) {
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public void generateFakeData() {
        List<Integer> clientsIds = new ArrayList<>();
        List<Integer> vehiclesIds = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            ClientDto clientDto = new ClientDto(getRandomName(), rnd.ints(0, 40).findFirst().getAsInt());
            clientsIds.add(addFakeClient(clientDto));
        }
        addMessage(SUCCESS, "Added new 30 clients");
        for (int i = 0; i < 60; i++) {

            Collections.shuffle(clientsIds);
            VehicleDto vehicleDto = createFakeVehicleDto(clientsIds.get(0).toString());
            vehiclesIds.add(addFakeVehicle(vehicleDto));
        }
        addMessage(SUCCESS, "Added new 60 vehicles");
    }

    private String getRandomName() {
        List<String> randomNames = Arrays.asList("Consuela Brumbaugh", "Magdalen Slocumb", "Modesta Alto", "Geoffrey Sandridge", "Ignacia Morace", "An Madson", "Angelica Wilder", "Kanisha Pinard", "Janae Eakin", "Rogelio Bohan", "Rhonda Yopp", "Hyon Jiang", "Linnie Embree", "Mathilda Burgard", "Foster Adkison", "Fernande Cranford", "Britteny Bevil", "Son Pharr", "Nanci Orourke", "Mandie Bernett", "Christene Delucia", "Elly Garbett", "Terra Cullinan", "Anita Grimes", "Lemuel Boyers", "Simona Mccrae", "Madelene Flickinger", "Dave Chadwell", "Adam Dirksen", "Piper Kirker");
        Collections.shuffle(randomNames);
        return randomNames.get(0);
    }

    private Integer addFakeVehicle(VehicleDto vehicleDto) {

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
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
                .setBodyOnFrame("VF1KC0JEF" + rnd.ints(1, 5000).findFirst().getAsInt() + "32")
                .setLastRevisionDate(LocalDate.now().minusMonths(rnd.ints(1, 5).findFirst().getAsInt()))
                .setItvDate(LocalDate.now().minusMonths(rnd.ints(1, 5).findFirst().getAsInt()))
                .setNextItvDate(LocalDate.now().plusYears(rnd.ints(1, 5).findFirst().getAsInt()))
                .setAirFilterReference(rnd.ints(1000, 100000000).findFirst().getAsInt() + "3")
                .setOilFilterReference(rnd.ints(1000, 100000000).findFirst().getAsInt() + "1")
                .setFuelFilter(rnd.ints(1000, 100000000).findFirst().getAsInt() + "2")
                .setMotorOil("5.5  5W30")
                .createVehicleDto();
    }

    private String getSaltString() {
        String saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 8) { // length of the random string.
            int index = rnd.nextInt(saltchars.length());
            salt.append(saltchars.charAt(index));
        }
        return salt.toString();
    }

    private Integer addFakeClient(ClientDto clientDto) {

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(Integer.class);
    }

    public void destroyTheWorld() {
        deleteAllVehicles();
        addMessage(SUCCESS, "All vehicles deleted");
        LOGGER.info("All vehicles deleted");
        deleteAllClients();
        addMessage(SUCCESS, "All clients deleted");
        LOGGER.info("All clients deleted");
    }

    private void deleteAllVehicles() {
        List<VehicleDto> vehicleDtoList = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<VehicleDto>>() {
                });
        vehicleDtoList.forEach(vehicleDto -> client.target(properties.getProperty(APP_BASE_URL) + API_PATH + VehicleApiController.VEHICLES + "/" + vehicleDto.getId())
                .request(MediaType.APPLICATION_JSON).delete());
    }

    private void deleteAllClients() {
        List<ClientDto> clientDtoLIst = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<ClientDto>>() {
                });
        clientDtoLIst.forEach(clientDto -> client.target(properties.getProperty(APP_BASE_URL) +  properties.getProperty(API_PATH) + ClientApiController.CLIENTS + "/" + clientDto.getId())
                .request(MediaType.APPLICATION_JSON).delete());
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

