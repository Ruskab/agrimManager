package api.business_controllers.unittest;

import api.business_controllers.ClientBusinessController;
import api.daos.DaoFactory;
import api.dtos.ClientDto;
import api.entity.Client;
import api.fake.DaoFactoryFake;
import api.object_mothers.ClientDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

//Unit Tests with Fake doubles
class ClientBusinessControllerTest {

    private ClientBusinessController clientBusinessController;

    @BeforeEach
    void setUp() {
        clientBusinessController = new ClientBusinessController();
        DaoFactory.setFactory(new DaoFactoryFake());
    }

    @Test
    void testCreateClient() {
        clientBusinessController.create(ClientDtoMother.clientDto());

        assertThat(DaoFactory.getFactory().getClientDao().findAll().count(), is(1L));
    }

    @Test
    void testReadClient() {
        clientBusinessController.create(ClientDtoMother.clientDto());

        ClientDto clientDto = clientBusinessController.read("1");

        assertThat(clientDto.getFullName(), is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(clientDto.getHours(), is(ClientDtoMother.HOURS));
    }

    @Test
    void testReadAllClients() {
        clientBusinessController.create(ClientDtoMother.clientDto());
        clientBusinessController.create(ClientDtoMother.clientDto());

        assertThat(clientBusinessController.readAll().size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    void testUpdateClient() {
        clientBusinessController.create(ClientDtoMother.clientDto());
        String createdClientFullName = DaoFactory.getFactory().getClientDao().read(1).get().getFullName();

        clientBusinessController.update("1", ClientDtoMother.withFullName("updatedName"));

        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(1);
        assertThat(createdClientFullName, is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

}