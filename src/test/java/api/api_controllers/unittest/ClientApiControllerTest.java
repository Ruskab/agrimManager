package api.api_controllers.unittest;

import api.api_controllers.ClientApiController;
import api.business_controllers.ClientBusinessController;
import api.dtos.ClientDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.object_mothers.ClientDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ClientApiControllerTest {

    @Mock
    private ClientBusinessController clientServiceMock;

    @InjectMocks
    private ClientApiController clientApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_client() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        doReturn(1).when(clientServiceMock).create(clientDto);

        Response response = clientApiController.create(clientDto);

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
    }

    @Test
    void create_client_should_create_client() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        doReturn(1).when(clientServiceMock).create(clientDto);
        ArgumentCaptor<ClientDto> argumentCaptor = ArgumentCaptor.forClass(ClientDto.class);

        clientApiController.create(clientDto);

        verify(clientServiceMock, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(clientDto));
    }

    @Test
    void create_client_without_client_dto_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> clientApiController.create(null));
    }

    @Test
    void create_client_without_clientDto_fullName_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> clientApiController.create(ClientDtoMother.withFullName(null)));
    }

    @Test
    void read_client_by_id_should_Throw_NotFoundException() {
        String nonExistentClientId = "999";
        doThrow(NotFoundException.class).when(clientServiceMock).read(nonExistentClientId);

        assertThrows(NotFoundException.class, () -> clientApiController.getById(nonExistentClientId));
    }

    @Test
    void update_client_without_clientDto_fullName_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> clientApiController.update("1", ClientDtoMother.withFullName(null)));
    }

    @Test
    void update_client_without_clientDto_clientDto_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> clientApiController.update("1", null));
    }

    @Test
    void delete_client_with_non_existent_client_id_should_throw_NotFoundException() {
        String nonExistentClientId = "999";
        doThrow(NotFoundException.class).when(clientServiceMock).delete(nonExistentClientId);

        assertThrows(NotFoundException.class, () -> clientApiController.delete(nonExistentClientId));
    }

}