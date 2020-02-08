package api.business_controllers;

import api.api_controllers.DeleteDataApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.entity.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class ClientBusinessControllerTest {

    private static final Logger LOGGER = LogManager.getLogger(ClientBusinessControllerTest.class);
    private static ClientBusinessController clientBusinessController;

    @BeforeAll
    static void prepare() {
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
    }

    @AfterAll
    static void deleteCreatedUsers() {
        new DeleteDataApiController().deleteAll();
    }

    @Test
    void testCreateClient() {
        int createdClientId = clientBusinessController.create(new ClientDto("fullName", 1));

        Optional<Client> createdClient = DaoFactory.getFactory().getClientDao().read(createdClientId);
        assertThat(createdClient.isPresent(), is(true));
        assertThat(createdClient.get().getId(), is(createdClientId));
        assertThat(createdClient.get().getFullName(), is("fullName"));
        assertThat(createdClient.get().getHours(), is(1));
    }

    @Test
    public void testReadMechanic() {
        int createdClientId = clientBusinessController.create(new ClientDto("fullName", 1));

        ClientDto clientDto = clientBusinessController.read(Integer.toString(createdClientId));

        assertThat(clientDto.getFullName(), is("fullName"));
        assertThat(clientDto.getHours(), is(1));
    }

    @Test
    public void testReadAllClients() {
        clientBusinessController.create(new ClientDto("fullName", 1));
        clientBusinessController.create(new ClientDto("fullName", 1));

        List<ClientDto> clients = this.clientBusinessController.readAll();

        assertThat(clients.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateClient() {
        int createdClientId = clientBusinessController.create(new ClientDto("fullName", 1));
        String createdClientFullName = DaoFactory.getFactory().getClientDao().read(createdClientId).get().getFullName();

        clientBusinessController.update(Integer.toString(createdClientId), new ClientDto("updatedName", 1));

        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdClientId);
        assertThat(createdClientFullName, is("fullName"));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

}