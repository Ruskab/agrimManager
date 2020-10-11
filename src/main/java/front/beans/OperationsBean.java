package front.beans;

import front.dtos.Client;
import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import front.gateways.ClientGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static front.util.SessionUtil.getAuthToken;


@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
public class OperationsBean {

    public static final String SUCCESS = "Success";
    private static final Logger LOGGER = LogManager.getLogger(OperationsBean.class);
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;
    private MechanicGateway mechanicGateway;
    private Mechanic mechanic;
    private DashboardModel model;
    private Random rnd = new SecureRandom();

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;


    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanicGateway = new MechanicGateway(getAuthToken());
        initDashboard();
        mechanic = sessionBean.getMechanic();

    }

    private void initDashboard() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("createFakeData");
        column2.addWidget("deleteAllData");
        column2.addWidget("createNewUser");
        model.addColumn(column1);
        model.addColumn(column2);
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event
                .getSenderColumnIndex());

        addMessage(message);
    }

    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent()
                .getId() + "'");

        addMessage(message);
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent()
                .getId() + " toggled", "Status:" + event.getVisibility().name());

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
            Client client = Client.builder()
                    .fullName(getRandomName())
                    .hours(rnd.ints(0, 40).findFirst().getAsInt())
                    .build();
            clientsIds.add(addFakeClient(client));
        }
        addMessage(SUCCESS, "Added new 30 clients");
        for (int i = 0; i < 60; i++) {
            Collections.shuffle(clientsIds);
            Vehicle vehicleDto = createFakeVehicle(clientsIds.get(0).toString());
            vehiclesIds.add(addFakeVehicle(vehicleDto));
        }
        addMessage(SUCCESS, "Added new 60 vehicles");

        for (int i = 0; i < 5; i++) {
            Collections.shuffle(vehiclesIds);
            Intervention intervention = createFakeInterventionDto(vehiclesIds.get(0).toString());
            addFakeIntervention(intervention);
        }

        addMessage(SUCCESS, "Added new 10 interventions to " + mechanic.getName());
    }


    private String getRandomName() {
        List<String> randomNames = Arrays.asList("Consuela Brumbaugh", "Magdalen Slocumb", "Modesta Alto", "Geoffrey Sandridge", "Ignacia Morace", "An Madson", "Angelica Wilder", "Kanisha Pinard", "Janae Eakin", "Rogelio Bohan", "Rhonda Yopp", "Hyon Jiang", "Linnie Embree", "Mathilda Burgard", "Foster Adkison", "Fernande Cranford", "Britteny Bevil", "Son Pharr", "Nanci Orourke", "Mandie Bernett", "Christene Delucia", "Elly Garbett", "Terra Cullinan", "Anita Grimes", "Lemuel Boyers", "Simona Mccrae", "Madelene Flickinger", "Dave Chadwell", "Adam Dirksen", "Piper Kirker");
        Collections.shuffle(randomNames);
        return randomNames.get(0);
    }

    private Integer addFakeVehicle(Vehicle vehicleDto) {
        return Integer.parseInt(vehicleGateway.create(vehicleDto));
    }

    private void addFakeIntervention(Intervention interventionDto) {
        mechanicGateway.createIntervention(mechanic, interventionDto);
    }

    private Vehicle createFakeVehicle(String clientId) {
        List<String> brands = Arrays.asList("Piaggio", "Suzuki", "Lexus", "Jaguar", "Iveco", "Lancia", "Volkswagen", "Bentley", "Tata", "BMW",
                "Seat", "Nissan", "Piaggio", "Suzuki", "Lexus", "Jaguar", "Iveco", "Lancia", "Volkswagen", "Bentley", "Tata",
                "BMW", "Seat", "Nissan", "Isuzu", "Aston Martin", "Smart", "Ferrari", "Citroen", "Skoda", "Opel", "Peugeot",
                "Kia", "Alfa Romeo", "Tesla", "Cadillac", "Mercedes-Benz", "SsangYong", "Lamborghini", "Jeep", "Audi",
                "Mitsubishi", "Land Rover", "Volvo", "Honda", "Ford", "Toyota", "Infiniti", "Chevrolet", "Renault", "Lotus",
                "Fiat", "Abarth", "Dacia", "Maserati", "Mazda", "Caterham", "Subaru", "Rolls-Royce", "Mini", "Lada", "Porsche", "KTM", "Morgan");

        Collections.shuffle(brands);

        return Vehicle.builder()
                .registrationPlate(getSaltString())
                .clientId(clientId)
                .brand(brands.get(0))
                .kms("03-03-2017 94744")
                .bodyOnFrame("VF1KC0JEF" + rnd.ints(1, 5000).findFirst().getAsInt() + "32")
                .lastRevisionDate(LocalDate.now().minusMonths(rnd.ints(1, 5).findFirst().getAsInt()))
                .itvDate(LocalDate.now().minusMonths(rnd.ints(1, 5).findFirst().getAsInt()))
                .nextItvDate(LocalDate.now().plusYears(rnd.ints(1, 5).findFirst().getAsInt()))
                .airFilterReference(rnd.ints(1000, 100000000).findFirst().getAsInt() + "3")
                .oilFilterReference(rnd.ints(1000, 100000000).findFirst().getAsInt() + "1")
                .fuelFilter(rnd.ints(1000, 100000000).findFirst().getAsInt() + "2")
                .motorOil("5.5  5W30")
                .build();
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

    private Intervention createFakeInterventionDto(String vehicleId) {
        List<String> titles = Arrays.asList("Ruedas", "Volante", "Capo", "Sistema", "Maletero", "Puerta", "Pintura", "faros", "Luces", "motor");
        Collections.shuffle(titles);
        int startTime = getStartTime();
        return Intervention.builder().title(titles.get(0)).interventionType("REPAIR").vehicleId(vehicleId).startTime(LocalDateTime.now().minusHours(startTime)).endTime(LocalDateTime.now().plusHours(1)).build();
    }

    private int getStartTime() {
        return rnd.ints(0, 8).findFirst().getAsInt();
    }

    private Integer addFakeClient(Client client) {
        return Integer.parseInt(clientGateway.create(client));
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
        List<Vehicle> vehicleDtoList = vehicleGateway.readAll();
        vehicleDtoList.forEach(vehicleDto -> vehicleGateway.delete(vehicleDto.getId()));
    }

    private void deleteAllClients() {
        List<Client> clients = clientGateway.readAll();
        clients.forEach(client -> clientGateway.delete(client.getId()));
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }


}

