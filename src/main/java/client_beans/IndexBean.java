package client_beans;

import api.dtos.ClientDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.List;


@ManagedBean
@RequestScoped
public class IndexBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ClientDto selectedClient;

    private ClientDto client;

    private List<ClientDto> clientDtoList;

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }


    public List<ClientDto> getClientDtoList() {
        return clientDtoList;
    }

    public void setClientDtoList(List<ClientDto> clientDtoList) {
        this.clientDtoList = clientDtoList;
    }

    public ClientDto getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(ClientDto selectedClient) {
        this.selectedClient = selectedClient;
    }

    @PostConstruct
    public void findClient() {

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client clientApi = Client.create(clientConfig);
        WebResource clientsResourse = clientApi.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");

        ClientResponse response = clientsResourse.get(ClientResponse.class);

        clientDtoList = response.getEntity(new GenericType<List<ClientDto>>() {
        });
    }
}
