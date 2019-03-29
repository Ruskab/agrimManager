package api;

import api.api_controllers.*;
import api.business_controllers.ClientBusinessController;
import api.business_controllers.InterventionBusinesssController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.*;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.*;
import com.sun.jersey.api.client.ClientResponse;
import http.*;
import http.Client;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherIT {

    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private static InterventionBusinesssController interventionBusinesssController;
    private static MechanicApiController mechanicApiController;
    private static RepairingPackApiController repairingPackApiController;
    private static InterventionApiController interventionApiController;
    private List<Integer> createdClients;
    private List<Integer> createdVehicles;
    private List<Integer> createdInterventions;
    private List<Integer> createdMechanics;
    private List<Integer> createdReparatingPacks;

    @BeforeAll
    public static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
        mechanicApiController = new MechanicApiController();
        repairingPackApiController = new RepairingPackApiController();
        interventionApiController = new InterventionApiController();
    }

    @BeforeEach
    public void init() {
        createdClients = new ArrayList<>();
        createdVehicles = new ArrayList<>();
        createdInterventions = new ArrayList<>();
        createdMechanics = new ArrayList<>();
        createdReparatingPacks = new ArrayList<>();
        createdClients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdClients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }

    @Test
    public void testCreateClient() {
        Response response = new ClientApiController().create(new ClientDto("fullNameTest", 4));
        Integer createdClientId = (Integer) response.getEntity();
        createdClients.add(createdClientId);

        Optional<api.entity.Client> createdClient = DaoFactory.getFactory().getClientDao().read((Integer) response.getEntity());
        assertThat(response.getStatus(), is(ClientResponse.Status.CREATED.getStatusCode()));
        assertThat(createdClient.get().getFullName(), is("fullNameTest"));
        assertThat(createdClient.get().getHours(), is(4));
    }

    @Test
    void testCreateVehicle() {
        int existentClientId = createdClients.get(0);
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(existentClientId), "AA1234AA");
        Response response = new VehicleApiController().create(vehicleDto);
        Integer id = (Integer) response.getEntity();
        createdVehicles.add(id);

        Optional<Vehicle> createdVehicle = DaoFactory.getFactory().getVehicleDao().read(id);
        assertThat(createdVehicle.get().getRegistrationPlate(), is("AA1234AA"));
        assertThat(createdVehicle.get().getClient().getId(), is(existentClientId));
        assertThat(createdVehicle.get().getBrand(), is("Opel"));
        assertThat(createdVehicle.get().getBodyOnFrame(), is(vehicleDto.getBodyOnFrame()));
        assertThat(createdVehicle.get().getItvDate(), is(vehicleDto.getItvDate()));
        assertThat(createdVehicle.get().getNextItvDate(), is(vehicleDto.getNextItvDate()));
        assertThat(createdVehicle.get().getKms(), is(vehicleDto.getKms()));
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
        assertThat(createdIntervention.get().getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Reparacion"));
        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(), LocalDate.now().plusDays(1))));
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
        assertThat(createdIntervention.get().getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Caffe"));
        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(), LocalDate.now().plusDays(1))));
        assertThat(createdIntervention.get().getState(), is(State.CAFFE));
    }

    @Test
    void testAddInterventionREPAIRtoMechanic() {
        int existentClientId = clientBusinessController.create(new ClientDto("fakeFullNameTest", 1));
        createdClients.add(existentClientId);
        int existentVehicleId = vehicleBusinessController.create(createVehicleDto(Integer.toString(existentClientId), "AA1234AA"));
        createdVehicles.add(existentVehicleId);
        InterventionDto interventionDto = createInterventionDto(Integer.toString(existentVehicleId));
        int existentMechanic = mechanicApiController.create(new MechanicDto("mechanic1", "secretPass"));
        createdMechanics.add(existentMechanic);

        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS + MechanicApiController.ID_INTERVENTIONS).expandPath(Integer.toString(existentMechanic)).body(interventionDto).post();
        new Client().submit(request).getBody();

        Optional<Mechanic> mechanic = DaoFactory.getFactory().getMechanicDao().read(existentMechanic);

        assertThat(mechanic.get().getInterventionList().get(0).getPeriod(), is(interventionDto.getPeriod()));
        assertThat(Integer.toString(mechanic.get().getInterventionList().get(0).getVehicle().get().getId()), is(interventionDto.getVehicleId()));
        assertThat(mechanic.get().getInterventionList().get(0).getState(), is(interventionDto.getState()));
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
    public void testCreateReparatingPack() {
        RepairingPackDto repairingPackDto = new RepairingPackDto();
        repairingPackDto.setInvoicedDate(LocalDate.now());
        repairingPackDto.setInvoicedHours(12);

        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS)
                .body(repairingPackDto).post();

        int id = (int) new Client().submit(request).getBody();
        createdReparatingPacks.add(id);

        Optional<RepairingPack> createdRepairingPack = DaoFactory.getFactory().getRepairingPackDao().read(id);
        assertThat(createdRepairingPack.get().getInvoicedDate(), is(LocalDate.now()));
        assertThat(createdRepairingPack.get().getInvoicedHours(), is(12));
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
    void testCreateRepairingPackInvalidRequest() {
        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).path("/invalid").body(null).post();

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
    void testCreateRepairingPackWithoutRepairingPackDto() {
        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).body(null).post();

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
    void testCreateRepairingPackWithEmptyInvoicedDateShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).body(new RepairingPackDto(null, 2)).post();

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
    void testUpdateInterventionRepairingPack() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(1).toString(), "AA1234AA")));
        int existentVehicleId = createdVehicles.get(0);
        InterventionDto interventionDto = createInterventionDto(Integer.toString(existentVehicleId));
        Integer createdInterventionId = interventionApiController.create(interventionDto);
        createdInterventions.add(createdInterventionId);
        RepairingPackDto repairingPackDto = new RepairingPackDto(LocalDate.now(), 3);
        String createdRepairingPackId = Integer.toString(repairingPackApiController.create(repairingPackDto));
        createdReparatingPacks.add(Integer.parseInt(createdRepairingPackId));

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)
                .expandPath(Integer.toString(createdInterventionId))
                .body(createdRepairingPackId)
                .patch();
        new Client().submit(request);
        Optional<api.entity.Intervention> updatedIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId);

        assertThat(updatedIntervention.get().getRepairingPack().get().getInvoicedDate(), is(repairingPackDto.getInvoicedDate()));
        assertThat(updatedIntervention.get().getRepairingPack().get().getInvoicedHours(), is(repairingPackDto.getInvoicedHours()));
    }

    @Test
    void testUpdateInterventionRepairingPackWithNotFoundInterventionShouldThrowNOT_FOUD() {
        RepairingPackDto repairingPackDto = new RepairingPackDto(LocalDate.now(), 3);
        String createdRepairingPackId = Integer.toString(repairingPackApiController.create(repairingPackDto));
        createdReparatingPacks.add(Integer.parseInt(createdRepairingPackId));

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)
                .expandPath(Integer.toString(99999))
                .body(createdRepairingPackId)
                .patch();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testUpdateInterventionRepairingPackWithNotValidInterventionShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)
                .expandPath("q9fs34")
                .body("99")
                .patch();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testUpdateInterventionRepairingPackWithNotValidRepairingPack() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdClients.get(1).toString(), "AA1234AA")));
        int existentVehicleId = createdVehicles.get(0);
        InterventionDto interventionDto = createInterventionDto(Integer.toString(existentVehicleId));
        Integer createdInterventionId = interventionApiController.create(interventionDto);
        createdInterventions.add(createdInterventionId);

        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)
                .expandPath(Integer.toString(createdInterventionId))
                .body("99999")
                .patch();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void testUpdateInterventionRepairingPackWithNotValidRepairingPackShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS + InterventionApiController.ID + InterventionApiController.REPAIRING_PACK)
                .expandPath("99")
                .body("q9f03")
                .patch();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
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
    void testUpdateClientBadRequestException() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").body(new ClientDto("updatedName", 1)).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
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

    @Test @Disabled
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
    void testDeleteClientBadRequestExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testDeleteVehicleNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testDeleteInterventionBadRequestExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
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
    void testReadRepairingPackByIdShoudThrowNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).path(RepairingPackApiController.ID)
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
    void testReadClientBadRequestExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadVehicleNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadInterventionNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).path(InterventionApiController.ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadRepairingPackNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).path(RepairingPackApiController.ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadClientVehiclesShoudThrowBadRequestExceptionWithInValidClientId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadClientVehiclesShoudThrowBadRequestExceptionWithoutClientId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testReadClient() {
        ClientDto clientDto = new ClientApiController().read(Integer.toString(createdClients.get(0)));

        assertThat(clientDto.getFullName(), is("fakeFullNameTest"));
        assertThat(clientDto.getHours(), is(1));
    }

    @Test
    void testReadRepairingPack() {
        RepairingPackDto expectedPackDto = new RepairingPackDto(LocalDate.now().minusDays(1), 2);
        createdReparatingPacks.add(repairingPackApiController.create(expectedPackDto));

        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).path(RepairingPackApiController.ID)
                .expandPath(Integer.toString(createdReparatingPacks.get(0))).get();
        RepairingPackDto repairingPackDto = (RepairingPackDto) new Client().submit(request).getBody();

        assertThat(repairingPackDto.getInvoicedDate(), is(expectedPackDto.getInvoicedDate()));
        assertThat(repairingPackDto.getInvoicedHours(), is(expectedPackDto.getInvoicedHours()));
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
        assertThat(interventionDto.getRepairingPackId(), is(expectedInterventionDto1.getRepairingPackId()));
    }

    @Test
    void testReadAllClient() {
        Stream clients = DaoFactory.getFactory().getClientDao().findAll();
        List<ClientDto> clientDtoList = new ClientApiController().readAll();

        assertThat((int) clients.count(), is(clientDtoList.size()));
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
        Stream persistedVehicles = DaoFactory.getFactory().getVehicleDao().findAll();

        assertThat((int) persistedVehicles.count(), is(vehicleDtoList.size()));
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

    @Test //todo se devuelven los paquetes en cualquier orden y eso va a veces solo
    void testReadAllRepairingPacks() {
        LocalDate date1 = LocalDate.now().minusDays(1);
        LocalDate date2 = LocalDate.now().minusDays(2);
        LocalDate date3 = LocalDate.now();

        RepairingPackDto repairingPackDto = new RepairingPackDto(date1, 2);
        RepairingPackDto repairingPackDto2 = new RepairingPackDto(date2, 3);
        RepairingPackDto repairingPackDto3 = new RepairingPackDto(date3, 4);

        createdReparatingPacks.add(repairingPackApiController.create(repairingPackDto));
        createdReparatingPacks.add(repairingPackApiController.create(repairingPackDto2));
        createdReparatingPacks.add(repairingPackApiController.create(repairingPackDto3));

        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).get();
        List<RepairingPackDto> repairingPackDtos = (List<RepairingPackDto>) new Client().submit(request).getBody();

        assertThat(repairingPackDtos.get(0).getInvoicedDate(), is(date1));
        assertThat(repairingPackDtos.get(1).getInvoicedDate(), is(date2));
        assertThat(repairingPackDtos.get(2).getInvoicedDate(), is(date3));
        assertThat(repairingPackDtos.get(0).getInvoicedHours(), is(2));
        assertThat(repairingPackDtos.get(1).getInvoicedHours(), is(3));
        assertThat(repairingPackDtos.get(2).getInvoicedHours(), is(4));
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
        ClientVehiclesDto clientVehiclesDto = new ClientApiController().clientVehiclesList(Integer.toString(createdClients.get(0)));

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
        return new InterventionDto("Reparacion", State.REPAIR, vehicleId, null,
                Period.between(LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    private InterventionDto createReparationInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", State.REPAIR, vehicleId, null,
                Period.between(LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    private InterventionDto createCaffeInterventionDto(String vehicleId) {
        return new InterventionDto("Caffe", State.CAFFE, vehicleId, null,
                Period.between(LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @AfterEach
    void clean() {
        createdMechanics.forEach(id -> DaoFactory.getFactory().getMechanicDao().deleteById(id));
        createdInterventions.forEach(id -> DaoFactory.getFactory().getInterventionDao().deleteById(id));
        createdVehicles.forEach(id -> DaoFactory.getFactory().getVehicleDao().deleteById(id));
        createdClients.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
        createdReparatingPacks.forEach(id -> DaoFactory.getFactory().getRepairingPackDao().deleteById(id));
    }
}
