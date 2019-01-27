package api;

import api.apiControllers.ClientApiController;
import api.dtos.ClientDto;
import http.Client;
import http.HttpException;
import http.HttpRequest;
import http.HttpStatus;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
}