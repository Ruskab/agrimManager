package api.business_controllers;

import api.api_controllers.DeleteDataApiController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.entity.Client;
import api.object_mothers.ClientDtoMother;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class ClientBusinessControllerTest {

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
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());

        Optional<Client> createdClient = DaoFactory.getFactory().getClientDao().read(createdClientId);
        assertThat(createdClient.isPresent(), is(true));
        assertThat(createdClient.get().getId(), is(createdClientId));
        assertThat(createdClient.get().getFullName(), is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(createdClient.get().getHours(), is(ClientDtoMother.HOURS));
    }

    @Test
    public void testReadClient() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());

        ClientDto clientDto = clientBusinessController.read(Integer.toString(createdClientId));

        assertThat(clientDto.getFullName(), is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(clientDto.getHours(), is(ClientDtoMother.HOURS));
    }

    @Test
    public void testReadAllClients() {
        clientBusinessController.create(ClientDtoMother.clientDto());
        clientBusinessController.create(ClientDtoMother.clientDto());

        List<ClientDto> clients = clientBusinessController.readAll();

        assertThat(clients.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateClient() {
        int createdClientId = clientBusinessController.create(ClientDtoMother.clientDto());
        String createdClientFullName = DaoFactory.getFactory().getClientDao().read(createdClientId).get().getFullName();

        clientBusinessController.update(Integer.toString(createdClientId), ClientDtoMother.withFullName("updatedName"));

        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdClientId);
        assertThat(createdClientFullName, is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

}