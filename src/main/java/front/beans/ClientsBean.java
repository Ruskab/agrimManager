package front.beans;

import front.dtos.Client;
import front.gateways.ClientGateway;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
public class ClientsBean implements Serializable {

    private List<Client> clients;
    private Client selectedClient;

    @PostConstruct
    public void init() {
        clients = new ClientGateway(getAuthToken()).readAll();
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

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

}