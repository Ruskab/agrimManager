package api.business_controllers;

import api.AgrimDomainFactory;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.Intervention;
import api.entity.InterventionType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;


public class InterventionBusinessControllerIT {

    private static InterventionBusinesssController interventionBusinesssController;
    private static VehicleBusinessController vehicleBusinessController;
    private static ClientBusinessController clientBusinessController ;
    private static List<Integer> createdVehicles;
    private static List<Integer> createdclients;
    private static List<Integer> createdInterventions;

    @BeforeAll
    static void prepare() {
        createdVehicles = new ArrayList<>();
        createdclients = new ArrayList<>();
        createdclients = new ArrayList<>();
        createdInterventions = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        vehicleBusinessController = new VehicleBusinessController();
        clientBusinessController = new ClientBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();

        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(Integer.toString(createdclients.get(0)), "111111")));
        createdVehicles.add(vehicleBusinessController.create(createVehicleDto(Integer.toString(createdclients.get(0)), "222222")));
        createdInterventions.add(interventionBusinesssController.create(AgrimDomainFactory.createInterventionDto(Integer.toString(createdVehicles.get(0)))));
        createdInterventions.add(interventionBusinesssController.create(AgrimDomainFactory.createCaffeInterventionDto()));
    }


    @Test
    void testCreateIntervention() {
        int createdClientId = clientBusinessController.create(new ClientDto("fakeFullName", 1));
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        int createdInterventionId = interventionBusinesssController.create(AgrimDomainFactory.createInterventionDto(Integer.toString(createdVehicleId)));

        createdclients.add(createdClientId);
        createdVehicles.add(createdVehicleId);
        createdInterventions.add(createdInterventionId);

        Optional<Intervention> createdIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId);
        assertThat(createdIntervention.get().getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Reparacion"));
//        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(), LocalDate.now().plusDays(1))));
        assertThat(createdIntervention.get().getInterventionType(), is(InterventionType.REPAIR));
        assertThat(createdIntervention.get().getVehicle().get().getId(), is(createdVehicleId));
    }

    @Test
    void testCreateInterventionCAFFE() {
        int createdInterventionId = interventionBusinesssController.create(AgrimDomainFactory.createCaffeInterventionDto());
        createdInterventions.add(createdInterventionId);

        Optional<Intervention> createdIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId);
        assertThat(createdIntervention.get().getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.get().getTitle(), is("Caffe"));
//        assertThat(createdIntervention.get().getPeriod(), is(Period.between(LocalDate.now(), LocalDate.now().plusDays(1))));
        assertThat(createdIntervention.get().getInterventionType(), is(InterventionType.CAFFE));
        assertThat(createdIntervention.get().getVehicle(), is(Optional.empty()));
    }

    @Test
    public void testReadIntervention() {
        InterventionDto interventionDto = interventionBusinesssController.read(Integer.toString(createdInterventions.get(0)));

        assertThat(interventionDto.getTitle(),is("Reparacion"));
    }

    @Test
    public void testReadAllVehicles() {
        List<InterventionDto> interventionDtos = interventionBusinesssController.readAll();
        assertThat(interventionDtos.size(),is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateIntervention(){
        //todo crear la funcionalidad
//        int createdClientId = clientBusinessController.create(new ClientDto("fakeFullName", 1));
//        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
//        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
//        InterventionDto interventionDto = createInterventionDto(Integer.toString(createdVehicleId));
//        int createdInterventionId = interventionBusinesssController.create(interventionDto);
//
//        String interventionTitle = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).get().getTitle();
//
//        createdclients.add(createdClientId);
//        createdVehicles.add(createdVehicleId);
//        createdInterventions.add(createdInterventionId);
//
//        interventionBusinesssController.update(Integer.toString(createdInterventionId), updateIntervention(interventionDto));
//        Optional<Intervention> updatedIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId);
//
//        assertThat(interventionTitle, is("Rapair"));
//        assertThat(updatedIntervention.get().getTitle(), is("updatedTitle"));
    }

    @AfterAll
    static void deleteCreatedUsers(){
        createdclients.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
        createdVehicles.forEach(id -> DaoFactory.getFactory().getVehicleDao().deleteById(id));
        createdInterventions.forEach(id -> DaoFactory.getFactory().getInterventionDao().deleteById(id));
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