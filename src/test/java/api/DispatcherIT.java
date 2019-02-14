package api;

import api.apiControllers.ClientApiController;
import api.businessControllers.ClientBusinessController;
import api.businessControllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.Vehicle;
import http.*;
import org.junit.Ignore;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherIT {

    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private List<Integer> createdUsers;
    private List<Integer> createdVehicles;

    @BeforeAll
    public static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
    }

    @BeforeEach
    public void init() {
        createdUsers = new ArrayList<>();
        createdVehicles = new ArrayList<>();
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }

    @Test
    public void testCreateClient() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS)
                .body(new ClientDto("fullNameTest", 4)).post();

        int id = (int) new Client().submit(request).getBody();
        createdUsers.add(id);

        Optional<api.entity.Client> createdClient = DaoFactory.getFactory().getClientDao().read(id);
        assertThat(createdClient.get().getFullName(), is("fullNameTest"));
        assertThat(createdClient.get().getHours(), is(4));
    }

    @Test
    void testCreateVehicle() {
        int existentClientId = createdUsers.get(0);
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
    void testCreateClientWithoutClientDtoFullName() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(new ClientDto(null, 1)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testCreateVehicleWithoutRegistrationPlate() {
        int existentClientId = createdUsers.get(0);
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
    void testUpdateClient() {
        int createdUserId = createdUsers.get(0);
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
        int createdClientId = createdUsers.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdClientId)).delete();
        new Client().submit(request);
        createdUsers.remove(0);
        Optional<api.entity.Client> deletedClient = DaoFactory.getFactory().getClientDao().read(createdClientId);

        assertThat(deletedClient.isPresent(), is(false));
    }

    @Test
    @Ignore("Foreign key error")
    void testDeleteClientWithVehiclesShouldThrowInternal_Server_Error() {
        int createdClientId = createdUsers.get(0);
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdUsers.get(0).toString(), "AA1234AA")));
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdClientId)).delete();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    void testDeleteVehicle() {
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(createdUsers.get(1).toString(), "AA1234AA")));
        int createdVehicleId = createdVehicles.get(0);

        HttpRequest request = HttpRequest.builder(VehicleApiController.VEHICLES).path(VehicleApiController.ID_ID)
                .expandPath(Integer.toString(createdVehicleId)).delete();
        new Client().submit(request);
        createdVehicles.remove(0);
        assertThat(DaoFactory.getFactory().getVehicleDao().read(createdVehicleId).isPresent(), is(false));
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
    void testReadClientByIdShoudThrowNotFoundExceptionWithValidId() {
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
        int createdUserId = createdUsers.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID)
                .expandPath(Integer.toString(createdUserId)).get();
        ClientDto clientDto = (ClientDto) new Client().submit(request).getBody();

        assertThat(clientDto.getFullName(), is("fakeFullNameTest"));
        assertThat(clientDto.getHours(), is(1));
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
        VehicleDto expectedVehicleDto1 = createVehicleDto(createdUsers.get(0).toString(), "AA1234AA");
        VehicleDto expectedVehicleDto2 = createVehicleDto(createdUsers.get(0).toString(), "BB1234BB");
        VehicleDto expectedVehicleDto3 = createVehicleDto(createdUsers.get(0).toString(), "CC1234CC");

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
    void testReadClientVehicles() {
        Integer expectedClientId = createdUsers.get(0);
        Integer otherClientId = createdUsers.get(1);
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
    void testReadClientVehiclesWithoutVehiclesShouldReturnClienVehicleDtoWithoutVehicles() {
        Integer expectedClientId = createdUsers.get(0);

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

    @AfterEach
    void clean() {
        createdVehicles.forEach(id -> DaoFactory.getFactory().getVehicleDao().deleteById(id));
        createdUsers.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
    }
}
