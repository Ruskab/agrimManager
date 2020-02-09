package api;

import api.api_controllers.ClientApiController;
import api.api_controllers.DeleteDataApiController;
import api.api_controllers.InterventionApiController;
import api.api_controllers.MechanicApiController;
import api.api_controllers.RepairingPackApiController;
import api.api_controllers.VehicleApiController;
import api.business_controllers.ClientBusinessController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.RepairingPack;
import http.Client;
import http.HttpException;
import http.HttpRequest;
import http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static api.AgrimDomainFactory.createClientDto;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DispatcherIT {

    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private static RepairingPackApiController repairingPackApiController;
    private static InterventionApiController interventionApiController;

    @BeforeAll
    public static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
        repairingPackApiController = new RepairingPackApiController();
        interventionApiController = new InterventionApiController();
    }

    @AfterAll
    static void deleteCreatedUsers() {
        new DeleteDataApiController().deleteAll();
    }

    @BeforeEach
    public void init() {
    }

    @Test
    public void testCreateReparatingPack() {
        RepairingPackDto repairingPackDto = new RepairingPackDto();
        repairingPackDto.setInvoicedDate(LocalDate.now());
        repairingPackDto.setInvoicedHours(12);

        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS)
                .body(repairingPackDto).post();

        int id = (int) new Client().submit(request).getBody();

        Optional<RepairingPack> createdRepairingPack = DaoFactory.getFactory().getRepairingPackDao().read(id);
        assertThat(createdRepairingPack.get().getInvoicedDate(), is(LocalDate.now()));
        assertThat(createdRepairingPack.get().getInvoicedHours(), is(12));
    }

    @Test
    void testCreateInterventionWithNoExistentVehicle() {
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(Integer.toString(99999));
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
        int existentClientId = clientBusinessController.create(createClientDto());
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
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(null);
        HttpRequest request = HttpRequest.builder(InterventionApiController.INTERVENTIONS).body(interventionDto).post();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithNullNameShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(MechanicDtoMother.withName(null)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithEmptyNameShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(MechanicDtoMother.withName("")).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithEmptyPasswordShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(MechanicDtoMother.withPassword("")).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateMechanicWithNullShouldThrowBAD_REQUEST() {
        HttpRequest request = HttpRequest.builder(MechanicApiController.MECHANICS).body(MechanicDtoMother.withPassword(null)).post();

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
    void testUpdateInterventionRepairingPack() {
        Integer createdClient = clientBusinessController.create(createClientDto());
        int existentVehicleId = vehicleBusinessController.create(createVehicleDto(createdClient.toString(), "AA1234AA"));
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(Integer.toString(existentVehicleId));
        Response response = interventionApiController.create(interventionDto);
        Integer createdInterventionId = (Integer) response.getEntity();
        RepairingPackDto repairingPackDto = new RepairingPackDto(LocalDate.now(), 3);
        String createdRepairingPackId = Integer.toString(repairingPackApiController.create(repairingPackDto));

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
        RepairingPackDto repairingPackDto = createRepairingPack();
        String createdRepairingPackId = Integer.toString(repairingPackApiController.create(repairingPackDto));

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
    void testDeleteVehicleWithoutClientId() {
        int createdVehicleId = vehicleBusinessController.create(createVehicleDto(null, "AA1234AA"));
        ;

        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath(Integer.toString(createdVehicleId)).delete();
        new Client().submit(request);

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
    void testReadRepairingPack() {
        RepairingPackDto expectedPackDto = new RepairingPackDto(LocalDate.now().minusDays(1), 2);
        int interventionPackId = repairingPackApiController.create(expectedPackDto);

        HttpRequest request = HttpRequest.builder(RepairingPackApiController.REPAIRING_PACKS).path(RepairingPackApiController.ID)
                .expandPath(Integer.toString(interventionPackId)).get();
        RepairingPackDto repairingPackDto = (RepairingPackDto) new Client().submit(request).getBody();

        assertThat(repairingPackDto.getInvoicedDate(), is(expectedPackDto.getInvoicedDate()));
        assertThat(repairingPackDto.getInvoicedHours(), is(expectedPackDto.getInvoicedHours()));
    }

    @Test
    void testReadClientVehicles() {
        Integer expectedClientId = clientBusinessController.create(createClientDto());
        Integer otherClientId = clientBusinessController.create(createClientDto());

        int createdVehicle1 = vehicleBusinessController.create(createVehicleDto(expectedClientId.toString(), "AA1234AA"));
        int createdVehicle2 = vehicleBusinessController.create(createVehicleDto(expectedClientId.toString(), "BB1234BB"));
        int createdVehicle3 = vehicleBusinessController.create(createVehicleDto(expectedClientId.toString(), "CC1234CC"));

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS + ClientApiController.ID_VEHICLES).expandPath(expectedClientId.toString()).get();
        ClientVehiclesDto clientVehiclesDtos = (ClientVehiclesDto) new Client().submit(request).getBody();

        assertThat(clientVehiclesDtos.getVehicles().size(), is(3));
        assertThat(clientVehiclesDtos.getVehicles(), containsInAnyOrder(createdVehicle1, createdVehicle2, createdVehicle3));
        assertThat(clientVehiclesDtos.getClientDto().getId(), is(expectedClientId));
    }

    @Test
    void testReadClientVehiclesWithoutVehiclesShouldReturnClientVehicleDtoWithoutVehicles() {
        Integer expectedClientId = clientBusinessController.create(createClientDto());

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

    private RepairingPackDto createRepairingPack() {
        return new RepairingPackDto(LocalDate.now(), 3);
    }

}
