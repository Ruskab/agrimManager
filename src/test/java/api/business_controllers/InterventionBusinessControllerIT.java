package api.business_controllers;

import api.api_controllers.DeleteDataApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.InterventionDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.Intervention;
import api.entity.InterventionType;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.InterventionDtoMother;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static api.entity.InterventionType.CAFFE;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;


public class InterventionBusinessControllerIT {

    private static InterventionBusinesssController interventionBusinesssController;
    private static VehicleBusinessController vehicleBusinessController;
    private static ClientBusinessController clientBusinessController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        vehicleBusinessController = new VehicleBusinessController();
        clientBusinessController = new ClientBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
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

    @AfterAll
    static void deleteCreatedUsers() {
        new DeleteDataApiController().deleteAll();
    }

    @Test
    void testCreateIntervention() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(Integer.toString(createdVehicleId));

        int createdInterventionId = interventionBusinesssController.create(interventionDto);

        Intervention createdIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).get();
        assertThat(createdIntervention.getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.getTitle(), is(InterventionDtoMother.FAKE_TITLE));
        assertThat(createdIntervention.getStartTime(), is(interventionDto.getStartTime()));
        assertThat(createdIntervention.getEndTime(), is(interventionDto.getEndTime()));
        assertThat(createdIntervention.getInterventionType(), is(InterventionType.REPAIR));
        assertThat(createdIntervention.getVehicle().get().getId(), is(createdVehicleId));
    }

    @Test
    void testCreateInterventionCAFFE() {
        InterventionDto caffeInterventionDto = InterventionDtoMother.cafe();
        int createdInterventionId = interventionBusinesssController.create(caffeInterventionDto);

        Intervention createdIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).get();
        assertThat(createdIntervention.getRepairingPack(), is(Optional.empty()));
        assertThat(createdIntervention.getTitle(), is(InterventionDtoMother.FAKE_TITLE));
        assertThat(createdIntervention.getStartTime(), is(caffeInterventionDto.getStartTime()));
        assertThat(createdIntervention.getEndTime(), is(caffeInterventionDto.getEndTime()));
        assertThat(createdIntervention.getInterventionType(), is(CAFFE));
        assertThat(createdIntervention.getVehicle(), is(Optional.empty()));
    }

    @Test
    public void testReadIntervention() {
        int createdInterventionId = interventionBusinesssController.create(InterventionDtoMother.cafe());

        InterventionDto interventionDto = interventionBusinesssController.read(Integer.toString(createdInterventionId));

        assertThat(interventionDto.getTitle(), is(InterventionDtoMother.FAKE_TITLE));
    }

    @Test
    public void testReadAllInterventions() {
        interventionBusinesssController.create(InterventionDtoMother.cafe());
        interventionBusinesssController.create(InterventionDtoMother.cafe());

        List<InterventionDto> interventionDtos = interventionBusinesssController.readAll();

        assertThat(interventionDtos.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateIntervention() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "222222");
        int createdVehicleId = vehicleBusinessController.create(vehicleDto);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(Integer.toString(createdVehicleId));
        int createdInterventionId = interventionBusinesssController.create(interventionDto);
        String createdInterventionTitle = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).get().getTitle();
        InterventionDto updatedTitleIntervention = InterventionDtoMother.cafe();
        updatedTitleIntervention.setTitle("new title");
        interventionBusinesssController.update(Integer.toString(createdInterventionId), updatedTitleIntervention);

        Intervention updatedIntervention = DaoFactory.getFactory().getInterventionDao().read(createdInterventionId).get();
        assertThat(createdInterventionTitle, is(InterventionDtoMother.FAKE_TITLE));
        assertThat(updatedIntervention.getTitle(), is("new title"));
    }


}