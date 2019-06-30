package client_beans;

import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.State;
import client_beans.clients.ClientGateway;
import client_beans.interventions.InterventionGateway;
import client_beans.mechanics.MechanicGateway;
import client_beans.vehicles.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omnifaces.util.Faces;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@ManagedBean
@ViewScoped
public class OperationsBean {

    private VehicleGateway vehicleGateway = new VehicleGateway();
    private ClientGateway clientGateway = new ClientGateway();
    private InterventionGateway interventionGateway = new InterventionGateway();
    private MechanicGateway mechanicGateway = new MechanicGateway();
    private MechanicDto mechanic;
    public static final String SUCCESS = "Success";
    private static final Logger LOGGER = LogManager.getLogger(OperationsBean.class);
    private DashboardModel model;
    private Random rnd = new Random();

    public MechanicDto getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    @PostConstruct
    public void init() {
        initDashboard();
        mechanic = (MechanicDto) Faces.getSession().getAttribute("mechanic");

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
        List<Integer> interventionsIds = new ArrayList<>();

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

        for (int i = 0; i < 10; i++){
            Collections.shuffle(vehiclesIds);
            InterventionDto interventionDto = createFakeInterventionDto(vehiclesIds.get(0).toString());
            interventionsIds.add(addFakeIntervention(interventionDto));
        }
    }




    private String getRandomName() {
        List<String> randomNames = Arrays.asList("Consuela Brumbaugh", "Magdalen Slocumb", "Modesta Alto", "Geoffrey Sandridge", "Ignacia Morace", "An Madson", "Angelica Wilder", "Kanisha Pinard", "Janae Eakin", "Rogelio Bohan", "Rhonda Yopp", "Hyon Jiang", "Linnie Embree", "Mathilda Burgard", "Foster Adkison", "Fernande Cranford", "Britteny Bevil", "Son Pharr", "Nanci Orourke", "Mandie Bernett", "Christene Delucia", "Elly Garbett", "Terra Cullinan", "Anita Grimes", "Lemuel Boyers", "Simona Mccrae", "Madelene Flickinger", "Dave Chadwell", "Adam Dirksen", "Piper Kirker");
        Collections.shuffle(randomNames);
        return randomNames.get(0);
    }

    private Integer addFakeVehicle(VehicleDto vehicleDto) {
        return Integer.parseInt(vehicleGateway.create(vehicleDto));
    }

    private Integer addFakeIntervention(InterventionDto interventionDto) {
        return Integer.parseInt(mechanicGateway.addIntervention(mechanic, interventionDto));
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

    private InterventionDto createFakeInterventionDto(String vehicleId) {
        List<String> titles = Arrays.asList("Ruedas", "Volante", "Capo", "Sistema", "Maletero", "Puerta", "Pintura", "faros", "Luces", "motor");
        Collections.shuffle(titles);
        int startTime = getStartTime();
        int endTime = getEndTime();
        InterventionDto interventionDto = new InterventionDto(titles.get(0), State.REPAIR, vehicleId, null, LocalDateTime.now().minusHours(startTime), LocalDateTime.now().plusHours(endTime));
        return interventionDto;
    }

    private int getEndTime() {
        return rnd.ints(1, 20).findFirst().getAsInt();
    }

    private int getStartTime() {
        return rnd.ints(0,8).findFirst().getAsInt();
    }

    private Integer addFakeClient(ClientDto clientDto) {
        return Integer.parseInt(clientGateway.create(clientDto));
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
        List<VehicleDto> vehicleDtoList = vehicleGateway.readAll();
        vehicleDtoList.forEach(vehicleDto -> vehicleGateway.delete(vehicleDto.getId()));
    }

    private void deleteAllClients() {
        List<ClientDto> clientDtoLIst = clientGateway.readAll();
        clientDtoLIst.forEach(clientDto -> clientGateway.delete(clientDto.getId()));
    }




}

