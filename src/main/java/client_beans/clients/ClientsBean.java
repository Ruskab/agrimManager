package client_beans.clients;

import api.dtos.ClientDto;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ClientsBean implements Serializable {

    private List<ClientDto> clients;
    private ClientDto selectedClient;

    @PostConstruct
    public void init() {
        clients = new ClientGateway().readAll();
    }

    public List<ClientDto> getClients() {
        return clients;
    }

    public void setClients(List<ClientDto> clients) {
        this.clients = clients;
    }

    public ClientDto getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(ClientDto selectedClient) {
        this.selectedClient = selectedClient;
    }

}