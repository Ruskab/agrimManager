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

import static client_beans.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class ClientsBean implements Serializable {

    private List<ClientDto> clients;
    private ClientDto selectedClient;

    @PostConstruct
    public void init() {
        clients = new ClientGateway(getAuthToken()).readAll();
    }

    public void openClientDialog() {
        List<String> clientParams = new ArrayList<>();
        clientParams.add(Integer.toString(selectedClient.getId()));
        Map<String, List<String>> params = new HashMap<>();
        params.put("parameters", clientParams);
        Map<String, Object> options = setupDialogOptions();
        PrimeFaces.current().dialog().openDynamic("clientsInfo", options, params);
    }

    public void openCreateClientDialog() {
        Map<String, Object> options = setupDialogOptions();
        PrimeFaces.current().dialog().openDynamic("createClient", options, null);
    }

    private Map<String, Object> setupDialogOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("resizable", true);
        options.put("responsive", true);
        options.put("draggable", true);
        options.put("height", 471);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        return options;
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