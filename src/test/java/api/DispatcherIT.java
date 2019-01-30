package api;

import api.apiControllers.ClientApiController;
import api.businessControllers.ClientBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import http.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DispatcherIT {

    private static ClientBusinessController clientBusinessController;
    private static List<Integer> createdUsers;

    @BeforeAll
    static void prepare() {
        createdUsers = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
    }

    @BeforeEach
    void init(){
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }

    @Test
    public void testCreateClient() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS)
                .body(new ClientDto("fullNameTest", 4)).post();

        int id = (int) new Client().submit(request).getBody();
        createdUsers.add(id);

        Optional<api.entity.Client> updatedUser = DaoFactory.getFactory().getClientDao().read(id);
        assertThat(updatedUser.get().getFullName(), is("fullNameTest"));
        assertThat(updatedUser.get().getHours(), is(4));
    }

    @Test
    public void testClientInvalidRequest() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path("/invalid").body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    public void testCreateClientWithoutClientDto() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(null).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    public void testCreateClientWithoutClientDtoFullName() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(new ClientDto(null, 1)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    public void testUpdateClient(){
        int createdUserId = createdUsers.get(0);
        String createdUserFullName = DaoFactory.getFactory().getClientDao().read(createdUserId).get().getFullName();

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(Integer.toString(createdUserId)).body(new ClientDto("updatedName", 3)).put();
        new Client().submit(request);
        Optional<api.entity.Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdUserId);

        assertThat(createdUserFullName, is("fakeFullNameTest"  ));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
        assertThat(updatedUser.get().getHours(), is(3));
    }

    @Test
    public void testUpdateClientWithoutClientDtoFullName() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(id).body(new ClientDto(null, 1)).put();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testUpdateClientWithoutClientDto() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(id).body(null).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testUpdateClientNotFoundException() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("s5FdeGf54D").body(new ClientDto("updatedName", 1)).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testDeleteClient(){
        int createdUserId = createdUsers.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(Integer.toString(createdUserId)).delete();
        new Client().submit(request);
        createdUsers.remove(0);
        Optional<api.entity.Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdUserId);

        assertThat(updatedUser.isPresent(), is(false));
    }

    @Test
    public void testDeleteClientNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("s5FdeGf54D").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testDeleteClientNotFoundExceptionWithValidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("99999").delete();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testReadClientByIdShoudThrowNotFoundExceptionWithValidId(){
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("99999").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testReadClientNotFoundExceptionWithInvalidId() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("s5FdeGf54D").get();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testReadClient(){
        int createdUserId = createdUsers.get(0);

        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(Integer.toString(createdUserId)).get();
        ClientDto clientDto = (ClientDto) new Client().submit(request).getBody();

        assertThat(clientDto.getFullName(), is("fakeFullNameTest"));
        assertThat(clientDto.getHours(), is(1));
    }

    @Test
    public void testReadAllClient(){
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).get();
        List<ClientDto> clientDtoList = (List<ClientDto>) new Client().submit(request).getBody();

        assertThat(clientDtoList.get(0).getFullName(), is("fakeFullNameTest"));
        assertThat(clientDtoList.get(0).getHours(), is(1));
        assertThat(clientDtoList.get(1).getFullName(), is("fakeFullNameTest2"));
        assertThat(clientDtoList.get(1).getHours(), is(2));
    }

    @AfterEach
    public void clean(){
        createdUsers.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
        createdUsers.clear();
    }
}
