package api.api_controllers.unittest;

import api.AgrimDomainFactory;
import api.api_controllers.VehicleApiController;
import api.business_controllers.VehicleBusinessController;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_vehicle() {
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle();
        doReturn(1).when(vehicleBusinessController).create(vehicleDto);

        vehicleApiController.create(vehicleDto);
    }

    @Test
    void create_vehicle_without_vehicle_dto_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> vehicleApiController.create(null));
    }

    @Test
    void create_vehicle_without_vehicleDto_registrationPlate_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> vehicleApiController.create(new VehicleDtoBuilder().byDefault().setRegistrationPlate(null).createVehicleDto()));
    }

    @Test
    void read_vehicle_by_id_given_non_existent_id_should_Throw_NotFoundException() {
        String nonExistentVehicleId = "999";
        doThrow(NotFoundException.class).when(vehicleBusinessController).read(nonExistentVehicleId);

        assertThrows(NotFoundException.class, () -> vehicleApiController.read(nonExistentVehicleId));
    }

    @Test
    void update_vehicle_without_vehicleDto_registationPlate_should_throw_FieldInvalidException() {
        assertThrows(FieldInvalidException.class, () -> vehicleApiController.update("1", new VehicleDtoBuilder().byDefault().setRegistrationPlate(null).createVehicleDto()));
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