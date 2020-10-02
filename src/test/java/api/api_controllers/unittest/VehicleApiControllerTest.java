package api.api_controllers.unittest;

import api.AgrimBackEndDomainFactory;
import api.api_controllers.VehicleApiController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.VehicleDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class VehicleApiControllerTest {

    @Mock
    private VehicleBusinessController vehicleBusinessController;

    @InjectMocks
    private VehicleApiController vehicleApiController;

    @BeforeEach
    void setUp() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_vehicle() {
        VehicleDto vehicleDto = AgrimBackEndDomainFactory.createVehicleDto();
        doReturn(1).when(vehicleBusinessController).create(vehicleDto);

        Response response = vehicleApiController.create(vehicleDto);

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

    }

    @Test
    void create_vehicle_without_vehicle_dto_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> vehicleApiController.create(null));
    }

    @Test
    void create_vehicle_without_vehicleDto_registrationPlate_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> {
            VehicleDto vehicleDto = VehicleDto.builder().registrationPlate(null).build();
            vehicleApiController.create(vehicleDto);
        });
    }

    @Test
    void read_vehicle_by_id_given_non_existent_id_should_Throw_NotFoundException() {
        String nonExistentVehicleId = "999";
        doThrow(NotFoundException.class).when(vehicleBusinessController).read(nonExistentVehicleId);

        assertThrows(NotFoundException.class, () -> vehicleApiController.getById(nonExistentVehicleId));
    }

    @Test
    void update_vehicle_without_vehicleDto_registationPlate_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> {
            VehicleDto vehicleDto = VehicleDto.builder().registrationPlate(null).build();
            vehicleApiController.update("1", vehicleDto);
        });
    }

    @Test
    void update_vehicle_without_vehicleDto_vehicleDto_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> vehicleApiController.update("1", null));
    }

    @Test
    void delete_vehicle_with_non_existent_vehicle_id_should_throw_NotFoundException() {
        String nonExistentVehicleId = "999";
        doThrow(NotFoundException.class).when(vehicleBusinessController).delete(nonExistentVehicleId);

        assertThrows(NotFoundException.class, () -> vehicleApiController.delete(nonExistentVehicleId));
    }

}