package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.MechanicDto;
import api.entity.Mechanic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class MechanicBusinessControllerIT {

    private static MechanicBusinessController mechanicBusinessControler;
    private static List<Integer> createdMechanics;
    private static MechanicDto createdMechanic1;
    private static MechanicDto createdMechanic2;

    @BeforeAll
    static void prepare() {
        createdMechanics = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        mechanicBusinessControler = new MechanicBusinessController();
        createdMechanic1 = new MechanicDto("fakeName", "password");
        createdMechanic2 = new MechanicDto("fakeName2", "password2");
        createdMechanics.add(mechanicBusinessControler.create(createdMechanic1));
        createdMechanics.add(mechanicBusinessControler.create(createdMechanic2));
    }

    @Test void testCreateMechanic() {
        int createdMechanicId= mechanicBusinessControler.create(new MechanicDto("mechanic1", "secretPass"));
        createdMechanics.add(createdMechanicId);

        Optional<Mechanic> createdMechanic = DaoFactory.getFactory().getMechanicDao().read(createdMechanicId);
        assertThat(createdMechanic.isPresent(), is(true));
        assertThat(createdMechanic.get().getId(), is(createdMechanicId));
        assertThat(createdMechanic.get().getName(), is("mechanic1"));
        assertThat(createdMechanic.get().getPassword(), is("secretPass"));
    }

    @Test
    public void testReadMechanic() {
        MechanicDto mechanicDtos = this.mechanicBusinessControler.read(Integer.toString(createdMechanics.get(0)));

        assertThat(mechanicDtos.getName(),is("fakeName"));
        assertThat(mechanicDtos.getPassword(),is("password"));
    }

    @Test
    public void testReadAllMechanic(){
        List<MechanicDto> mechanicDtos = mechanicBusinessControler.readAll();

        assertThat(mechanicDtos.size(), greaterThanOrEqualTo(2));
    }

    @Test
    public void testDeleteMechanic(){
        int createdMechanicId = createdMechanics.get(0);
        int deleteMechanic = mechanicBusinessControler.create(new MechanicDto("toDelele", "toDelete"));
        DaoFactory.getFactory().getMechanicDao().read(createdMechanicId).get();

        mechanicBusinessControler.delete(Integer.toString(deleteMechanic));

        Optional<Mechanic> deletedMechanic = DaoFactory.getFactory().getMechanicDao().read(deleteMechanic);
        assertThat(deletedMechanic.isPresent(), is(false));
    }

    @AfterAll
    static void deleteCreatedUsers(){
        createdMechanics.forEach(id -> DaoFactory.getFactory().getMechanicDao().deleteById(id));
    }
}