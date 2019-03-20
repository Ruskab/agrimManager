package client_beans;

import api.dtos.ClientDto;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;


@ManagedBean
@RequestScoped
public class IndexBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientId;

    private ClientDto client;

    public IndexBean(){
        //CDI
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void findClient(){
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client clientApi = Client.create(clientConfig);
        WebResource clientsResourse = clientApi.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + clientId);

        com.sun.jersey.api.client.ClientResponse response = clientsResourse.get(com.sun.jersey.api.client.ClientResponse.class);
        client = response.getEntity(ClientDto.class);
    }
}
