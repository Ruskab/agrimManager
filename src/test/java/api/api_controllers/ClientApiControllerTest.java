package api.api_controllers;

import api.dtos.ClientDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.jersey.api.json.JSONConfiguration;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;


class ClientApiControllerTest {

    Client client;
    static ClientConfig clientConfig;

    @BeforeAll
    static void init(){
        clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(
            JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    }

    @BeforeEach
    void setUp() {
        client = Client.create(clientConfig);
    }

    @Test
    void create() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");

        ClientResponse response = clientsResourse.type("application/json").post(ClientResponse.class, clientDto);

        String createdClient = response.getEntity(String.class);
        assertThat(response.getStatus(), is(201));

        client = Client.create(clientConfig);
        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + createdClient);
        response = clientsResourse.get(ClientResponse.class);
        ClientDto createdClientDto = response.getEntity(ClientDto.class);

        assertThat(response.getStatus(), is(200));
        assertThat(createdClientDto.getFullName(), is("fullNameTest"));
        assertThat(createdClientDto.getHours(), is(4));
    }

    @Test
    void read() {
        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/2185");
        ClientResponse response = clientsResourse.get(ClientResponse.class);
        ClientDto clientDto = response.getEntity(ClientDto.class);

        assertThat(response.getStatus(), is(200));
    }
}