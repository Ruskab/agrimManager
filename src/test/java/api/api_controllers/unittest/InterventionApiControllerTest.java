package api.api_controllers.unittest;

import api.api_controllers.InterventionApiController;
import api.business_controllers.InterventionBusinesssController;
import api.dtos.InterventionDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.object_mothers.InterventionDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InterventionApiControllerTest {

    @Mock
    private InterventionBusinesssController interventionBusinessController;

    @InjectMocks
    private InterventionApiController interventionApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_intervention() {
        InterventionDto interventionDto = InterventionDtoMother.interventionDto();
        doReturn(1).when(interventionBusinessController).create(interventionDto);

        interventionApiController.create(interventionDto);
    }

    @Test
    void create_intervention_should_create_intervention() {
        InterventionDto interventionDto = InterventionDtoMother.interventionDto();
        doReturn(1).when(interventionBusinessController).create(interventionDto);
        ArgumentCaptor<InterventionDto> argumentCaptor = ArgumentCaptor.forClass(InterventionDto.class);

        interventionApiController.create(interventionDto);

        verify(interventionBusinessController, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(interventionDto));
    }

    @Test
    void create_intervention_without_intervention_dto_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> interventionApiController.create(null));
    }

    @Test
    void create_intervention_without_interventionDto_interventionType_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> interventionApiController.create(InterventionDtoMother.withInterventionType(null)));
    }

    @Test
    void read_intervention_by_id_should_Throw_NotFoundException() {
        String nonExistentInterventionId = "999";
        doThrow(NotFoundException.class).when(interventionBusinessController).read(nonExistentInterventionId);

        assertThrows(NotFoundException.class, () -> interventionApiController.read(nonExistentInterventionId));
    }

    @Test
    void update_intervention_without_interventionDto_interventionType_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> interventionApiController.update("1", InterventionDtoMother.withInterventionType(null)));
    }

    @Test
    void update_intervention_without_interventionDto_interventionDto_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> interventionApiController.update("1", null));
    }

    @Test
    void delete_intervention_with_non_existent_intervention_id_should_throw_NotFoundException() {
        String nonExistentInterventionId = "999";
        doThrow(NotFoundException.class).when(interventionBusinessController).delete(nonExistentInterventionId);

        assertThrows(NotFoundException.class, () -> interventionApiController.delete(nonExistentInterventionId));
    }

}