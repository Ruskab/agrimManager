package client_beans.clients;

import api.dtos.ClientDto;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ClientsBean implements Serializable {

    private List<ClientDto> clients;
    private ClientDto selectedClient;

    @PostConstruct
    public void init() {
        clients = new ClientGateway().readAll();
    }

    public void openClientDialog() {
        List<String> clientParams = new ArrayList<>();
        clientParams.add(Integer.toString(selectedClient.getId()));
        Map<String, List<String>> params = new HashMap<>();
        params.put("parameters", clientParams);
        Map<String,Object> options = new HashMap<>();
        options.put("resizable", false);
        options.put("contentHeight", 320);
        PrimeFaces.current().dialog().openDynamic("clientsInfo", options, params);
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