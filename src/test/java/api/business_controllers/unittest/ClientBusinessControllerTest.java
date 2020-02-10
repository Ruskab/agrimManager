package api.business_controllers.unittest;

import api.business_controllers.ClientBusinessController;
import api.daos.DaoFactory;
import api.daos.fake.DaoFactoryFake;
import api.dtos.ClientDto;
import api.entity.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

//Unit Tests with Fake doubles
public class ClientBusinessControllerTest {

    private static ClientBusinessController clientBusinessController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryFake());
        clientBusinessController = new ClientBusinessController();
    }

    @Test
    void testCreateClient() {
        clientBusinessController.create(new ClientDto("fullName", 1));

        assertThat(DaoFactory.getFactory().getClientDao().findAll().count(), is(1L));
    }

    @Test
    public void testReadMechanic() {
        clientBusinessController.create(new ClientDto("fullName", 1));

        ClientDto clientDto = clientBusinessController.read("1");

        assertThat(clientDto.getFullName(), is("fullName"));
        assertThat(clientDto.getHours(), is(1));
    }

    @Test
    public void testReadAllClients() {
        clientBusinessController.create(new ClientDto("fullName", 1));
        clientBusinessController.create(new ClientDto("fullName", 1));

        assertThat(clientBusinessController.readAll().size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateClient() {
        clientBusinessController.create(new ClientDto("fullName", 1));
        String createdClientFullName = DaoFactory.getFactory().getClientDao().read(1).get().getFullName();

        clientBusinessController.update("1", new ClientDto("updatedName", 1));

        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(1);
        assertThat(createdClientFullName, is("fullName"));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

}