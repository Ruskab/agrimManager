package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.entity.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class ClientBusinessControllerTest {

    private static ClientBusinessController clientBusinessController;
    private static List<Integer> createdclients;

    @BeforeAll
    static void prepare() {
        createdclients = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdclients.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }


    @Test
    void testCreateClient() {
        int createdClientId= clientBusinessController.create(new ClientDto("fullName", 1));
        createdclients.add(createdClientId);

        Optional<Client> createdClient = DaoFactory.getFactory().getClientDao().read(createdClientId);
        assertThat(createdClient.isPresent(), is(true));
        assertThat(createdClient.get().getId(), is(createdClientId));
        assertThat(createdClient.get().getFullName(), is("fullName"));
        assertThat(createdClient.get().getHours(), is(1));
    }

    @Test
    public void testReadMechanic() {
        ClientDto clientDto = clientBusinessController.read(Integer.toString(createdclients.get(0)));

        assertThat(clientDto.getFullName(),is("fakeFullNameTest"));
        assertThat(clientDto.getHours(),is(1));
    }

    @Test
    public void testReadAllClients() {
        List<ClientDto> clients = this.clientBusinessController.readAll();
        assertThat(clients.size(),is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void testUpdateClient(){
        int createdClientId = createdclients.get(0);
        String createdClientFullName = DaoFactory.getFactory().getClientDao().read(createdClientId).get().getFullName();

        clientBusinessController.update(Integer.toString(createdClientId), new ClientDto("updatedName",1));
        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdClientId);

        assertThat(createdClientFullName, is("fakeFullNameTest"  ));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

    @AfterAll
    static void deleteCreatedUsers(){
        createdclients.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
    }




}