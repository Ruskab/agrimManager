package api.business_controllers.unittest;

import api.business_controllers.MechanicBusinessController;
import api.daos.DaoFactory;
import api.dtos.MechanicDto;
import api.fake.DaoFactoryFake;
import api.object_mothers.MechanicDtoMother;
import api.stub.DaoFactoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class MechanicBusinessControllerTest {

    private MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    @BeforeEach
    void setUp() {
        DaoFactory.setFactory(new DaoFactoryStub());
    }

    @Test
    void testCreateMechanic() {
        DaoFactory.setFactory(new DaoFactoryFake());
        mechanicBusinessController.create(MechanicDtoMother.mechanicDto());

        assertThat(DaoFactory.getFactory().getMechanicDao().findAll().count(), is(1L));
    }

    @Test
    void testReadMechanic() {
        MechanicDto mechanicDto = mechanicBusinessController.read("1");

        assertThat(mechanicDto.getName(), is(MechanicDtoMother.FAKE_NAME));
        assertThat(mechanicDto.getPassword(), is(MechanicDtoMother.FAKE_PASSWORD));
    }

    @Test
    void testFindByNameMechanic() {
        List<MechanicDto> mechanicDtos = mechanicBusinessController.searchBy(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);

        assertThat(mechanicDtos.get(0).getName(), is(MechanicDtoMother.FAKE_NAME));
        assertThat(mechanicDtos.get(0).getPassword(), is(MechanicDtoMother.FAKE_PASSWORD));
    }

    @Test
    void testReadAllMechanic() {
        assertThat(mechanicBusinessController.readAll().size(), greaterThanOrEqualTo(2));
    }

}