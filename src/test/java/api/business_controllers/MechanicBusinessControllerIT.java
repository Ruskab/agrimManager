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

public class MechanicBusinessControllerIT {

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
        assertThat(createdMechanic.get().getPassword(), is(MechanicDtoMother.FAKE_PASSWORD));
    }

    @Test
    public void testReadMechanic() {
        int createdMechanicId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        MechanicDto mechanicDto = mechanicBusinessController.read(Integer.toString(createdMechanicId));

        assertThat(mechanicDto.getName(), is(MechanicDtoMother.FAKE_NAME));
        assertThat(mechanicDto.getPassword(), is(MechanicDtoMother.FAKE_PASSWORD));
    }

    @Test
    public void testFindByNameMechanic() {
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        List<MechanicDto> mechanicDtos = mechanicBusinessController.searchBy(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);

        assertThat(mechanicDtos.get(0).getName(), is(MechanicDtoMother.FAKE_NAME));
        assertThat(mechanicDtos.get(0).getPassword(), is(MechanicDtoMother.FAKE_PASSWORD));
    }

    @Test
    public void testReadAllMechanic() {
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        assertThat(mechanicBusinessController.readAll().size(), greaterThanOrEqualTo(2));
    }

    @Test
    public void testDeleteMechanic() {
        int mechanicToDeleteId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        mechanicBusinessController.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
    }

    @Test
    public void testDeleteMechanicWithInterventionsShouldDeleteInterventions() {
        Integer mechanicToDeleteId = mechanicBusinessController.create(MechanicDtoMother.mechanicDto());
        mechanicBusinessController.createIntervention(mechanicToDeleteId.toString(), InterventionDtoMother.cafe());
        mechanicBusinessController.createIntervention(mechanicToDeleteId.toString(), InterventionDtoMother.cafe());

        mechanicBusinessController.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
        assertThat(interventionBusinesssController.readAll().isEmpty(), is(true));
    }

}