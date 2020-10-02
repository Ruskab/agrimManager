package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.VehicleDto;
import api.entity.Vehicle;
import api.object_mothers.ClientDtoMother;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class VehicleBusinessControllerIT {

    private static VehicleBusinessController vehicleBusinessController;
    private static ClientBusinessController clientBusinessController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        vehicleBusinessController = new VehicleBusinessController();
        clientBusinessController = new ClientBusinessController();
    }

    private static VehicleDto createVehicleDto(String clientId, String registrationPlate) {
        return VehicleDto.builder().
                registrationPlate(registrationPlate)
                .clientId(clientId)
                .brand("Opel")
                .kms("03-03-2017 94744")
                .bodyOnFrame("VF1KC0JEF31065732")
                .lastRevisionDate(LocalDate.now().minusMonths(2))
                .itvDate(LocalDate.now().minusMonths(3))
                .nextItvDate(LocalDate.now().plusYears(1))
                .airFilterReference("1813029400")
                .oilFilterReference("1812344000")
                .fuelFilter("181315400")
                .motorOil("5.5  5W30")
                .build();
    }

    @Test
    void testCreateVehicle() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);

        Vehicle createdVehicle = DaoFactory.getFactory().getVehicleDao().read(createdVehicleId).get();
        assertThat(createdVehicle.getRegistrationPlate(), is("222222"));
        assertThat(createdVehicle.getClient().getId(), is(createdClientId));
        assertThat(createdVehicle.getBrand(), is("Opel"));
        assertThat(createdVehicle.getBodyOnFrame(), is(vehicleDto.getBodyOnFrame()));
        assertThat(createdVehicle.getItvDate(), is(vehicleDto.getItvDate()));
        assertThat(createdVehicle.getNextItvDate(), is(vehicleDto.getNextItvDate()));
        assertThat(createdVehicle.getKms(), is(vehicleDto.getKms()));
        assertThat(createdVehicle.getLastRevisionDate(), is(vehicleDto.getLastRevisionDate()));
        assertThat(createdVehicle.getAirFilterReference(), is(vehicleDto.getAirFilterReference()));
        assertThat(createdVehicle.getFuelFilter(), is(vehicleDto.getFuelFilter()));
        assertThat(createdVehicle.getOilFilterReference(), is(vehicleDto.getOilFilterReference()));
        assertThat(createdVehicle.getMotorOil(), is(vehicleDto.getMotorOil()));
    }

    @Test
    public void testReadVehicle() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        int createdVehicleId = vehicleBusinessController.create(createVehicleDto(Integer.toString(createdClientId), "222222"));

        VehicleDto vehicleDto = vehicleBusinessController.read(Integer.toString(createdVehicleId));

        assertThat(vehicleDto.getRegistrationPlate(), is("222222"));
    }

    @Test
    public void testReadAllVehicles() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        clientBusinessController.create(ClientDtoMother.clientDto());
        vehicleBusinessController.create(createVehicleDto(Integer.toString(createdClientId), "222222"));
        vehicleBusinessController.create(createVehicleDto(Integer.toString(createdClientId), "222222"));

        List<VehicleDto> vehicleDtos = vehicleBusinessController.readAll();

        assertThat(vehicleDtos.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateVehicle() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        String createdBodyOnFrame = DaoFactory.getFactory()
                .getVehicleDao()
                .read(createdVehicleId)
                .get()
                .getBodyOnFrame();

        vehicleBusinessController.update(Integer.toString(createdVehicleId), updateVehicle(vehicleDto));
        Optional<Vehicle> updatedVehicle = DaoFactory.getFactory().getVehicleDao().read(createdVehicleId);

        assertThat(createdBodyOnFrame, is("VF1KC0JEF31065732"));
        assertThat(updatedVehicle.get().getBodyOnFrame(), is("updatedBodyOnFrame"));
    }

    private VehicleDto updateVehicle(VehicleDto vehicleDto) {
        vehicleDto.setBodyOnFrame("updatedBodyOnFrame");
        return vehicleDto;
    }


}