package api.businessControllers;

import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.entity.Client;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class ClientBusinessControllerIT {

    private static ClientBusinessController clientBusinessController;
    private static List<Integer> createdUsers;

    @BeforeAll
    static void prepare() {
        createdUsers = new ArrayList<>();
        DaoFactory.setFactory(new DaoFactoryHibr());
        clientBusinessController = new ClientBusinessController();
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest", 1)));
        createdUsers.add(clientBusinessController.create(new ClientDto("fakeFullNameTest2", 2)));
    }

    @Test
    public void testReadClient() {
        List<ClientDto> clients = this.clientBusinessController.readAll();
        assertThat(clients.size(),is(greaterThan(0)));
    }

    @Test
    public void testUpdateUser(){
        int createdUserId = createdUsers.get(0);
        String createdUserFullName = DaoFactory.getFactory().getClientDao().read(createdUserId).get().getFullName();

        clientBusinessController.update(Integer.toString(createdUserId), new ClientDto("updatedName",1));
        Optional<Client> updatedUser = DaoFactory.getFactory().getClientDao().read(createdUserId);

        assertThat(createdUserFullName, is("fakeFullNameTest"  ));
        assertThat(updatedUser.get().getFullName(), is("updatedName"));
    }

    @AfterAll
    static void deleteCreatedUsers(){
        createdUsers.forEach(id -> DaoFactory.getFactory().getClientDao().deleteById(id));
    }




}