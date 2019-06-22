package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;


public class VehicleBusinessControllerIT {

    private static VehicleBusinessController vehicleBusinessController;
    private static ClientBusinessController clientBusinessController ;
    private static List<Integer> createdVehicles;
    private static List<Integer> createdclients;

    @BeforeAll
    static void prepare() {
        createdVehicles = new ArrayList<>();
        createdclients = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        vehicleBusinessController = new VehicleBusinessController();
        clientBusinessController = new ClientBusinessController();

        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(Integer.toString(createdclients.get(0)), "111111")));
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(Integer.toString(createdclients.get(0)), "222222")));
    }


    @Test
    void testCreateVehicle() {
        int createdClientId = clientBusinessController.create(new ClientDto("fakeFullName", 1));
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        createdclients.add(createdClientId);
        createdVehicles.add(createdVehicleId);

        Optional<Vehicle> createdVehicle = DaoFactory.getFactory().getVehicleDao().read(createdVehicleId);
        assertThat(createdVehicle.isPresent(), is(true));
        assertThat(createdVehicle.get().getRegistrationPlate(), is("222222"));
        assertThat(createdVehicle.get().getClient().getId(), is(createdClientId));
        assertThat(createdVehicle.get().getBrand(), is("Opel"));
        assertThat(createdVehicle.get().getBodyOnFrame(), is(vehicleDto.getBodyOnFrame()));
        assertThat(createdVehicle.get().getItvDate(), is(vehicleDto.getItvDate()));
        assertThat(createdVehicle.get().getNextItvDate(), is(vehicleDto.getNextItvDate()));
        assertThat(createdVehicle.get().getKms(), is(vehicleDto.getKms()));
        assertThat(createdVehicle.get().getLastRevisionDate(), is(vehicleDto.getLastRevisionDate()));
        assertThat(createdVehicle.get().getAirFilterReference(), is(vehicleDto.getAirFilterReference()));
        assertThat(createdVehicle.get().getFuelFilter(), is(vehicleDto.getFuelFilter()));
        assertThat(createdVehicle.get().getOilFilterReference(), is(vehicleDto.getOilFilterReference()));
        assertThat(createdVehicle.get().getMotorOil(), is(vehicleDto.getMotorOil()));
    }

    @Test
    public void testReadVehicle() {
        VehicleDto vehicleDto = vehicleBusinessController.read(Integer.toString(createdVehicles.get(0)));

        assertThat(vehicleDto.getRegistrationPlate(),is("111111"));
    }

    @Test
    public void testReadAllVehicles() {
        List<VehicleDto> vehicleDtos = vehicleBusinessController.readAll();
        assertThat(vehicleDtos.size(),is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateVehicle(){
        int createdClientId = clientBusinessController.create(new ClientDto("fakeFullName", 1));
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        String createdBodyOnFrame = DaoFactory.getFactory().getVehicleDao().read(createdVehicleId).get().getBodyOnFrame();
        createdclients.add(createdClientId);
        createdVehicles.add(createdVehicleId);

        vehicleBusinessController.update(Integer.toString(createdVehicleId), updateVehicle(vehicleDto));
        Optional<Vehicle> updatedVehicle = DaoFactory.getFactory().getVehicleDao().read(createdVehicleId);

        assertThat(createdBodyOnFrame, is("VF1KC0JEF31065732"));
        assertThat(updatedVehicle.get().getBodyOnFrame(), is("updatedBodyOnFrame"));
    }

    private VehicleDto updateVehicle(VehicleDto vehicleDto) {
        vehicleDto.setBodyOnFrame("updatedBodyOnFrame");
        return vehicleDto;
    }

    @AfterAll
    static void deleteCreatedUsers(){
        createdclients.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
        createdVehicles.forEach(id -> DaoFactory.getFactory().getVehicleDao().deleteById(id));
    }


    private static VehicleDto createVehicleDto(String clientId, String registrationPlate) {
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



}