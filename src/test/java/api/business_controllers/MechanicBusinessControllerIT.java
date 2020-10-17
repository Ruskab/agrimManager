package api.business_controllers;

import api.api_controllers.DeleteDataApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.MechanicDto;
import api.entity.Mechanic;
import api.object_mothers.InterventionDtoMother;
import api.object_mothers.MechanicDtoMother;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class MechanicBusinessControllerIT {

    private static MechanicBusinessController mechanicBusinessController;
    private static InterventionBusinesssController interventionBusinesssController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        mechanicBusinessController = new MechanicBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
    }

    @AfterAll
    static void deleteCreatedUsers() {
        new DeleteDataApiController().deleteAll();
    }

    @Test
    void testCreateMechanic() {
        Integer createdMechanicId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        Optional<Mechanic> createdMechanic = DaoFactory.getFactory().getMechanicDao().read(createdMechanicId);
        assertThat(createdMechanic.isPresent(), is(true));
        assertThat(createdMechanic.get().getId(), is(createdMechanicId));
        assertThat(createdMechanic.get().getName(), is(MechanicDtoMother.FAKE_NAME));
    }

    @Test
    void testReadMechanic() {
        int createdMechanicId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        MechanicDto mechanicDto = mechanicBusinessController.read(Integer.toString(createdMechanicId));

        assertThat(mechanicDto.getName(), is(MechanicDtoMother.FAKE_NAME));
    }

    @Test
    void testFindByNameMechanic() {
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        List<MechanicDto> mechanicDtos = mechanicBusinessController.searchBy(MechanicDtoMother.FAKE_NAME);

        assertThat(mechanicDtos.get(0).getName(), is(MechanicDtoMother.FAKE_NAME));
    }

    @Test
    void testReadAllMechanic() {
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        assertThat(mechanicBusinessController.readAll().size(), greaterThanOrEqualTo(2));
    }

    @Test
    void testDeleteMechanic() {
        int mechanicToDeleteId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        mechanicBusinessController.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
    }

    @Test
    void testDeleteMechanicWithInterventionsShouldDeleteInterventions() {
        Integer mechanicToDeleteId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());
        mechanicBusinessController.createIntervention(mechanicToDeleteId.toString(), InterventionDtoMother.cafe());
        mechanicBusinessController.createIntervention(mechanicToDeleteId.toString(), InterventionDtoMother.cafe());

        mechanicBusinessController.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
        assertThat(interventionBusinesssController.readAll().isEmpty(), is(true));
    }

}