package api;

import api.apiControllers.ClientApiController;
import api.apiControllers.InterventionApiController;
import api.businessControllers.ClientBusinessController;
import api.businessControllers.InterventionBusinesssController;
import api.businessControllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.*;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.Intervention;
import api.entity.Mechanic;
import api.entity.State;
import api.entity.Vehicle;
import http.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherIT {

    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private static InterventionBusinesssController interventionBusinesssController;
    private static MechanicApiController mechanicApiController;
    private List<Integer> createdClients;
    private List<Integer> createdVehicles;
    private List<Integer> createdInterventions;
    private List<Integer> createdMechanics;

    @BeforeAll
    public static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
        mechanicApiController = new MechanicApiController();
    }

    @BeforeEach
    public void init() {
        createdClients = new ArrayList<>();
        createdVehicles = new ArrayList<>();
        createdInterventions = new ArrayList<>();
        createdMechanics = new ArrayList<>();
        createdClients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdClients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }

    @Test
    public void testCreateClient() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS)
                .body(new ClientDto("fullNameTest", 4)).post();

        int id = (int) new Client().submit(request).getBody();
        createdClients.add(id);

        Optional<api.entity.Client> createdClient = DaoFactory.getFactory().getClientDao().read(id);
        assertThat(createdClient.get().getFullName(), is("fullNameTest"));
        assertThat(createdClient.get().getHours(), is(4));
    }

    @Test
    void testCreateVehicle() {
        int existentClientId = createdClients.get(0);
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(existentClientId), "AA1234AA");
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).body(vehicleDto).post();
        int id = (int) new Client().submit(request).getBody();
        createdVehicles.add(id);

        Optional<Vehicle> createdVehicle = DaoFactory.getFactory().getVehicleDao().read(id);
        assertThat(createdVehicle.get().getRegistrationPlate(), is("AA1234AA"));
        assertThat(createdVehicle.get().getClient().getId(), is(existentClientId));
        assertThat(createdVehicle.get().getBrand(), is("Opel"));
        assertThat(createdVehicle.get().getBodyOnFrame(), is(vehicleDto.getBodyOnFrame()));
        assertThat(createdVehicle.get().getItvDate(), is(vehicleDto.getItvDate()));
        assertThat(createdVehicle.get().getNextItvDate(), is(vehicleDto.getNextItvDate()));
        assertThat(createdVehicle.get().getKMS(), is(vehicleDto.getKMS()));
        assertThat(createdVehicle.get().getLastRevisionDate(), is(vehicleDto.getLastRevisionDate()));
        assertThat(createdVehicle.get().getAirFilterReference(), is(vehicleDto.getAirFilterReference()));
        assertThat(createdVehicle.get().getFuelFilter(), is(vehicleDto.getFuelFilter()));
        assertThat(createdVehicle.get().getOilFilterReference(), is(vehicleDto.getOilFilterReference()));
        assertThat(createdVehicle.get().getMotorOil(), is(vehicleDto.getMotorOil()));
    }

    @Test
    void testCreateInterventionREPAIR() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(1).toString(), "AA1234AA")));
        int existentVehicleId = createdVehicles.get(0);

        InterventionDto interventionDto = createInterventionDto(Integer.toString(existentVehicleId));
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        int id = (int) new Client().submit(request).getBody();
        createdInterventions.add(id);

        Optional<Intervention> createdIntervention = DaoFactory.getFactory().getInterventionDao().read(id);
        assertThat(createdIntervention.get().getWork(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Reparacion"));
        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(),LocalDate.now().plusDays(1))));
        assertThat(createdIntervention.get().getState(), is(State.REPAIR));
        assertThat(createdIntervention.get().getVehicle().get().getId(), is(existentVehicleId));
    }

    @Test
    void testCreateInterventionCAFFE() {
        InterventionDto interventionDto = createCaffeInterventionDto(null);
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        int id = (int) new Client().submit(request).getBody();
        createdInterventions.add(id);

        Optional<Intervention> createdIntervention = DaoFactory.getFactory().getInterventionDao().read(id);
        assertThat(createdIntervention.get().getWork(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Caffe"));
        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(),LocalDate.now().plusDays(1))));
        assertThat(createdIntervention.get().getState(), is(State.CAFFE));
    }

    @Test
    public void testCreateMechanic() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS)
                .body(new MechanicDto("mechanic1", "secretPass")).post();

        int id = (int) new Client().submit(request).getBody();
        createdMechanics.add(id);

        Optional<Mechanic> createdMechanic = DaoFactory.getFactory().getMechanicDao().read(id);
        assertThat(createdMechanic.get().getName(), is("mechanic1"));
        assertThat(createdMechanic.get().getPassword(), is("secretPass"));
    }

    @Test
    void testCreateInterventionWithNoExistentVehicle() {
        InterventionDto interventionDto = createInterventionDto(Integer.toString(99999));
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateClientInvalidRequest() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path("/invalid").body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateVehicleInvalidRequest() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path("/invalid").body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicInvalidRequest() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).path("/invalid").body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateClientWithoutClientDto() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }



    @Test
    void testCreateVehicleWithoutClientDto() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithoutMechanicDto() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateClientWithoutClientDtoFullName() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(new ClientDto(null, 1)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateVehicleWithoutRegistrationPlate() {
        int existentClientId = createdClients.get(0);
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(existentClientId), null);
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).body(vehicleDto).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateVehicleWithoutClientId() {
        VehicleDto vehicleDto = createVehicleDto(null, "AA1234AA");
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).body(vehicleDto).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateInterventionRepairWithoutVehicleId() {
        InterventionDto interventionDto = createInterventionDto(null);
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateInterventionCaffeWithVehicleIdShouldThrowBadRequest() {
        InterventionDto interventionDto = createCaffeInterventionDto("23");
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithNullNameShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(new MechanicDto(null, "1234")).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithEmptyNameShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(new MechanicDto("", "1234")).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithEmptyPasswordShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(new MechanicDto("mechanic1", "")).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithNullShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(new MechanicDto("mechanic1", null)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testUpdateClient() {
        int createdUserId = createdClients.get(0);
        String createdUserFullName = DaoFactory.getFactory().getClientDao().read(createdUserId).get().getFullName();

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdUserId)).body(new ClientDto("updatedName", 3)).put();
        new Client().submit(request);
        Optional<api.entity.Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdUserId);

        assertThat(createdUserFullName, is("fakeFullNameTest"));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
        assertThat(updatedUser.get().getHours(), is(3));
    }

    @Test
    void testUpdateClientWithoutClientDtoFullName() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(id).body(new ClientDto(null, 1)).put();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testUpdateClientWithoutClientDto() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(id).body(null).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testUpdateClientNotFoundException() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").body(new ClientDto("updatedName", 1)).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteClient() {
        int createdClientId = createdClients.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdClientId)).delete();
        new Client().submit(request);
        createdClients.remove(0);
        Optional<api.entity.Client> deletedClient = DaoFactory.getFactory().getClientDao().read(createdClientId);

        assertThat(deletedClient.isPresent(), is(false));
    }

    @Test
    //@Ignore("Foreign key error")
    void testDeleteClientWithVehiclesShouldThrowInternal_Server_Error() {
        int createdClientId = createdClients.get(0);
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(0).toString(), "AA1234AA")));
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdClientId)).delete();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    void testDeleteVehicle() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(1).toString(), "AA1234AA")));
        int createdVehicleId = createdVehicles.get(0);

        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath(Integer.toString(createdVehicleId)).delete();
        new Client().submit(request);
        createdVehicles.remove(0);
        assertThat(DaoFactory.getFactory().getVehicleDao().read(createdVehicleId).isPresent(), is(false));
    }

    @Test
    void testDeleteCAFFEIntervention() {
        createdInterventions.add(interventionBusinesssController.create(createCaffeInterventionDto(null)));
        int createdInterventionId = createdInterventions.get(0);

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath(Integer.toString(createdInterventionId)).delete();
        new Client().submit(request);
        createdInterventions.remove(0);
        assertThat(DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).isPresent(), is(false));
    }

    @Test
    void testDeleteREPAIRIntervention() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(1).toString(), "AA1234AA")));
        createdInterventions.add(interventionBusinesssController.create(createInterventionDto(createdVehicles.get(0).toString())));
        int createdInterventionId = createdInterventions.get(0);

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath(Integer.toString(createdInterventionId)).delete();
        new Client().submit(request);
        createdInterventions.remove(0);
        assertThat(DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).isPresent(), is(false));
    }

    @Test
    void testDeleteVehicleWithoutClientId() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(null, "AA1234AA")));
        int createdVehicleId = createdVehicles.get(0);

        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath(Integer.toString(createdVehicleId)).delete();
        new Client().submit(request);
        createdVehicles.remove(0);
        assertThat(DaoFactory.getFactory().getVehicleDao().read(createdVehicleId).isPresent(), is(false));
    }

    @Test
    void testDeleteClientNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteVehicleNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteInterventionNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteClientNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("99999").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDeleteVehicleNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("99999").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testReadClientByIdShouldThrowNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("99999").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadVehicleByIdShoudThrowNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("99999").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadInterventionByIdShoudThrowNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath("99999").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadClientVehiclesShoudThrowNotFoundExceptionWithValidClientId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath("99999").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadClientNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadVehicleNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadInterventionNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadClientVehiclesShoudThrowNotFoundExceptionWithInValidClientId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testReadClientVehiclesShoudThrowNotFoundExceptionWithoutClientId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND)); //todo change to bad request
    }

    @Test
    void testReadClient() {
        int createdUserId = createdClients.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdUserId)).get();
        ClientDto clientDto = (ClientDto) new Client().submit(request).getBody();

        assertThat(clientDto.getFullName(), is("fakeFullNameTest"));
        assertThat(clientDto.getHours(), is(1));
    }

    @Test
    void testReadIntervention() {
        VehicleDto expectedVehicleDto1 = createVehicleDto(createdClients.get(0).toString(), "AA1234AA");
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto1));
        InterventionDto expectedInterventionDto1 = createInterventionDto(createdVehicles.get(0).toString());
        createdInterventions.add(interventionBusinesssController.create(expectedInterventionDto1));
        int createdInterventionId = createdInterventions.get(0);
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath(Integer.toString(createdInterventionId)).get();

        InterventionDto interventionDto = (InterventionDto) new Client().submit(request).getBody();

        assertThat(interventionDto.getId(), is(createdInterventionId));
        assertThat(interventionDto.getVehicleId(), is(createdVehicles.get(0).toString()));
        assertThat(interventionDto.getState(), is(State.REPAIR));
        assertThat(interventionDto.getPeriod(), is(expectedInterventionDto1.getPeriod()));
        assertThat(interventionDto.getTitle(), is(expectedInterventionDto1.getTitle()));
        assertThat(interventionDto.getWorkId(), is(expectedInterventionDto1.getWorkId()));
    }

    @Test
    void testReadAllClient() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).get();
        List<ClientDto> clientDtoList = (List<ClientDto>) new Client().submit(request).getBody();

        assertThat(clientDtoList.get(0).getFullName(), is("fakeFullNameTest"));
        assertThat(clientDtoList.get(0).getHours(), is(1));
        assertThat(clientDtoList.get(1).getFullName(), is("fakeFullNameTest2"));
        assertThat(clientDtoList.get(1).getHours(), is(2));
    }

    @Test
    void testReadAllVehicles() {
        VehicleDto expectedVehicleDto1 = createVehicleDto(createdClients.get(0).toString(), "AA1234AA");
        VehicleDto expectedVehicleDto2 = createVehicleDto(createdClients.get(0).toString(), "BB1234BB");
        VehicleDto expectedVehicleDto3 = createVehicleDto(createdClients.get(0).toString(), "CC1234CC");

        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto1));
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto2));
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto3));

        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).get();
        List<VehicleDto> vehicleDtoList = (List<VehicleDto>) new Client().submit(request).getBody();

        assertThat(vehicleDtoList.get(0).getRegistrationPlate(), is(expectedVehicleDto1.getRegistrationPlate()));
        assertThat(vehicleDtoList.get(1).getRegistrationPlate(), is(expectedVehicleDto2.getRegistrationPlate()));
        assertThat(vehicleDtoList.get(2).getRegistrationPlate(), is(expectedVehicleDto3.getRegistrationPlate()));
    }

    @Test
    void testReadAllInterventions() {
        VehicleDto expectedVehicleDto1 = createVehicleDto(createdClients.get(0).toString(), "AA1234AA");
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto1));
        InterventionDto expectedInterventionDto1 = createInterventionDto(createdVehicles.get(0).toString());
        InterventionDto expectedInterventionDto2 = createReparationInterventionDto(createdVehicles.get(0).toString());
        InterventionDto expectedInterventionDto3 = createCaffeInterventionDto(null);
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto1));
        createdInterventions.add(interventionBusinesssController.create(expectedInterventionDto1));
        createdInterventions.add(interventionBusinesssController.create(expectedInterventionDto2));
        createdInterventions.add(interventionBusinesssController.create(expectedInterventionDto3));

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).get();
        List<Integer> interventionIds = ((List<InterventionDto>) new Client().submit(request).getBody()).stream()
                .map(interventionDto -> interventionDto.getId()).collect(Collectors.toList());

        assertThat(interventionIds, is(createdInterventions));
    }

    @Test
    void testReadClientVehicles() {
        Integer expectedClientId = createdClients.get(0);
        Integer otherClientId = createdClients.get(1);
        VehicleDto expectedVehicleDto1 = createVehicleDto(expectedClientId.toString(), "AA1234AA");
        VehicleDto expectedVehicleDto2 = createVehicleDto(expectedClientId.toString(), "BB1234BB");
        VehicleDto expectedVehicleDto3 = createVehicleDto(expectedClientId.toString(), "CC1234CC");
        VehicleDto expectedVehicleDto4 = createVehicleDto(otherClientId.toString(), "DD1234DD");
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto1));
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto2));
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto3));
        createdVehicles.add(vehicleBusinessController.create(expectedVehicleDto4));

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath(expectedClientId.toString()).get();
        ClientVehiclesDto clientVehiclesDtos = (ClientVehiclesDto) new Client().submit(request).getBody();

        assertThat(clientVehiclesDtos.getVehicles(),
                allOf(
                        contains(createdVehicles.get(0), createdVehicles.get(1), createdVehicles.get(2)),
                        not(contains(createdVehicles.get(3))))
        );
        assertThat(clientVehiclesDtos.getClientDto().getId(), is(expectedClientId));
    }

    @Test
    void testReadClientVehiclesWithoutVehiclesShouldReturnClientVehicleDtoWithoutVehicles() {
        Integer expectedClientId = createdClients.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath(expectedClientId.toString()).get();
        ClientVehiclesDto clientVehiclesDtos = (ClientVehiclesDto) new Client().submit(request).getBody();

        assertThat(clientVehiclesDtos.getClientDto().getId(), is(expectedClientId));
        assertThat(clientVehiclesDtos.getVehicles(), is(Collections.emptyList()));
    }

    private VehicleDto createVehicleDto(String clientId, String registrationPlate) {
        return new VehicleDtoBuilder()
                .setRegistrationPlate(registrationPlate)
                .setClientId(clientId)
                .setBrand("Opel")
                .setKMS("03-03-2017 94744")
                .setBodyOnFrame("VF1KC0JEF31065732")
                .setLastRevisionDate(LocalDate.now().minusMonths(2))
                .setItvDate(LocalDate.now().minusMonths(3))
                .setNextItvDate(LocalDate.now().plusYears(1))
                .setAirFilterReference("1813029400")
                .setOilFilterReference("1812344000")
                .setFuelFilter("181315400")
                .setMotorOil("5.5  5W30")
                .createVehicleDto();
    }

    private InterventionDto createInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", State.REPAIR,vehicleId,null,
                Period.between(LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    private InterventionDto createReparationInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", State.REPAIR,vehicleId,null,
                Period.between(LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    private InterventionDto createCaffeInterventionDto(String vehicleId) {
        return new InterventionDto("Caffe", State.CAFFE,vehicleId,null,
                Period.between(LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    @AfterEach
    void clean() {
        createdInterventions.forEach(id -> DaoFactory.getFactory().getInterventionDao().deleteById(id));
        createdVehicles.forEach(id -> DaoFactory.getFactory().getVehicleDao().deleteById(id));
        createdClients.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
        createdMechanics.forEach(id -> DaoFactory.getFactory().getMechanicDao().deleteById(id));
    }
}
