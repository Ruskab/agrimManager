package api.api_controllers;

import api.dtos.ClientDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.sun.jersey.api.json.JSONConfiguration;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@Disabled("Falta configurar Travis para que arranque el servidor...")
class ClientApiControllerTest {

    Client client;
    static ClientConfig clientConfig;

    @BeforeAll
    static void init() {
        clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(
                JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    }

    @BeforeEach
    void setUp() {
        client = Client.create(clientConfig);
    }

    @Test
    void create_and_read_clientDto() {
        //Create client
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");


        //read client
        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + "2190");

        ClientResponse response = clientsResourse.get(ClientResponse.class);
        ClientDto createdClientDto = response.getEntity(ClientDto.class);

        assertThat(response.getStatus(), is(200));
        assertThat(createdClientDto.getFullName(), is("fullNameTest"));
        assertThat(createdClientDto.getHours(), is(4));
    }

    @Test
    void delete_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");
        ClientResponse response = clientsResourse.type("application/json").post(ClientResponse.class, clientDto);
        String createdClient = response.getEntity(String.class);
        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + createdClient);

        response = clientsResourse.delete(ClientResponse.class);

        assertThat(response.getStatus(), is(204));

    }

    @Test
    void update_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");
        ClientResponse response = clientsResourse.type("application/json").post(ClientResponse.class, clientDto);
        String createdClient = response.getEntity(String.class);
        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + createdClient);
        clientDto.setFullName("newFullName");
        clientDto.setHours(5);

        response = clientsResourse.type("application/json").put(ClientResponse.class, clientDto);

        assertThat(response.getStatus(), is(200));

        //read client
        response = clientsResourse.get(ClientResponse.class);
        ClientDto updatedClientDto = response.getEntity(ClientDto.class);

        assertThat(response.getStatus(), is(200));
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));

    }
}