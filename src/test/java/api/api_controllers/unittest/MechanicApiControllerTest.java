package api.api_controllers.unittest;

import api.api_controllers.MechanicApiController;
import api.business_controllers.MechanicBusinessController;
import api.dtos.MechanicDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.object_mothers.MechanicDtoMother;
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

class MechanicApiControllerTest {

    @Mock
    private MechanicBusinessController mechanicBusinessController;

    @InjectMocks
    private MechanicApiController mechanicApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_mechanic() {
        MechanicDto mechanicDto = MechanicDtoMother.mechanicDto();
        doReturn(1).when(mechanicBusinessController).create(mechanicDto);

        Response response = mechanicApiController.create(mechanicDto);

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

    }

    @Test
    void create_mechanic_should_create_mechanic() {
        MechanicDto mechanicDto = MechanicDtoMother.mechanicDto();
        doReturn(1).when(mechanicBusinessController).create(mechanicDto);
        ArgumentCaptor<MechanicDto> argumentCaptor = ArgumentCaptor.forClass(MechanicDto.class);

        mechanicApiController.create(mechanicDto);

        verify(mechanicBusinessController, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(mechanicDto));
    }

    @Test
    void create_mechanic_without_mechanic_dto_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> mechanicApiController.create(null));
    }

    @Test
    void create_mechanic_without_name_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> mechanicApiController.create(MechanicDtoMother.withName(null)));
    }

    @Test
    void create_mechanic_without_password_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> mechanicApiController.create(MechanicDtoMother.withPassword(null)));
    }

    @Test
    void read_mechanic_by_id_should_Throw_NotFoundException() {
        String nonExistentMechanicId = "999";
        doThrow(NotFoundException.class).when(mechanicBusinessController).read(nonExistentMechanicId);

        assertThrows(NotFoundException.class, () -> mechanicApiController.getById(nonExistentMechanicId));
    }

    @Test
    void delete_mechanic_with_non_existent_mechanic_id_should_throw_NotFoundException() {
        String nonExistentMechanicId = "999";
        doThrow(NotFoundException.class).when(mechanicBusinessController).delete(nonExistentMechanicId);

        assertThrows(NotFoundException.class, () -> mechanicApiController.delete(nonExistentMechanicId));
    }

}