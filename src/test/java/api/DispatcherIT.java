package api;

import api.apiControllers.ClientApiController;
import api.dtos.ClientDto;
import http.Client;
import http.HttpException;
import http.HttpRequest;
import http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DispatcherIT {

    @Test
    void testCreateClient() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS)
                .body(new ClientDto("fullNameTest",1)).post();

        int id = (int) new Client().submit(request).getBody();

        assertThat(id, is(not(0)));
    }

    @Test
    void testClientInvalidRequest() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path("/invalid").body(null).post();

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
    void testCreateClientWithoutClientDtoFullName() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).body(new ClientDto(null,1)).post();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(HttpStatus.BAD_REQUEST, is(exception.getHttpStatus()));
    }

    @Test
    void testUpdateClientWithoutClientDtoFullName() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(id).body(new ClientDto(null,1)).put();

        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testUpdateUserWithoutUserDto() {
        String id = "1";
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath(id).body(null).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testUpdateUserNotFoundException() {
        HttpRequest request = HttpRequest.builder(ClientApiController.CLIENTS).path(ClientApiController.ID_ID)
                .expandPath("s5FdeGf54D").body(new ClientDto("updatedName",1)).put();
        HttpException exception = assertThrows(HttpException.class, () -> new Client().submit(request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
