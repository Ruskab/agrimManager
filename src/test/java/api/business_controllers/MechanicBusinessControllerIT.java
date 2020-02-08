package api.business_controllers;

import api.AgrimDomainFactory;
import api.api_controllers.DeleteDataApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.MechanicDto;
import api.entity.Mechanic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static api.AgrimDomainFactory.createCaffeInterventionDto;
import static api.AgrimDomainFactory.createMechanic;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class MechanicBusinessControllerIT {

    private static MechanicBusinessController mechanicBusinessControler;
    private static InterventionBusinesssController interventionBusinesssController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        mechanicBusinessControler = new MechanicBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();

    }

    @AfterAll
    static void deleteCreatedUsers() {
        new DeleteDataApiController().deleteAll();
    }

    @Test
    void testCreateMechanic() {
        int createdMechanicId = mechanicBusinessControler.create(new MechanicDto("mechanic1", "secretPass"));

        Optional<Mechanic> createdMechanic = DaoFactory.getFactory().getMechanicDao().read(createdMechanicId);
        assertThat(createdMechanic.isPresent(), is(true));
        assertThat(createdMechanic.get().getId(), is(createdMechanicId));
        assertThat(createdMechanic.get().getName(), is("mechanic1"));
        assertThat(createdMechanic.get().getPassword(), is("secretPass"));
    }

    @Test
    public void testReadMechanic() {
        int createdMechanicId = mechanicBusinessControler.create(new MechanicDto("mechanic1", "secretPass"));

        MechanicDto mechanicDtos = mechanicBusinessControler.read(Integer.toString(createdMechanicId));

        assertThat(mechanicDtos.getName(), is("mechanic1"));
        assertThat(mechanicDtos.getPassword(), is("secretPass"));
    }

    @Test
    public void testFindByNameMechanic() {
        mechanicBusinessControler.create(createMechanic());

        List<MechanicDto> mechanicDtos = mechanicBusinessControler.findBy("mechanic");

        assertThat(mechanicDtos.get(0).getName(), is("mechanic"));
        assertThat(mechanicDtos.get(0).getPassword(), is("secretPass"));
    }

    @Test
    public void testReadAllMechanic() {
        mechanicBusinessControler.create(createMechanic());
        mechanicBusinessControler.create(createMechanic());
        List<MechanicDto> mechanicDtos = mechanicBusinessControler.readAll();

        assertThat(mechanicDtos.size(), greaterThanOrEqualTo(2));
    }

    @Test
    public void testDeleteMechanic() {
        int mechanicToDeleteId = mechanicBusinessControler.create(new MechanicDto("toDelele", "toDelete"));
        DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId).get();

        mechanicBusinessControler.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
    }

    @Test
    public void testDeleteMechanicWithInterventionsShouldDeleteInterventions() {
        Integer mechanicToDeleteId = mechanicBusinessControler.create(createMechanic());
        DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId).get();
        mechanicBusinessControler.createIntervention(mechanicToDeleteId.toString(), createCaffeInterventionDto() );
        mechanicBusinessControler.createIntervention(mechanicToDeleteId.toString(), createCaffeInterventionDto() );

        mechanicBusinessControler.delete(Integer.toString(mechanicToDeleteId));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(mechanicToDeleteId);
        assertThat(deletedMechanic.isPresent(), is(false));
        assertThat(new InterventionBusinesssController().readAll().isEmpty(), is(true));
    }

}