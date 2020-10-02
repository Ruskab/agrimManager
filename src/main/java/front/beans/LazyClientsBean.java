package front.beans;

import front.dtos.Client;
import front.gateways.ClientGateway;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean(name = "lazyClientsBean")
@ViewScoped
public class LazyClientsBean implements Serializable {

    private LazyDataModel<Client> lazyModel;

    private Client selectedClient;

    private ClientGateway clientGateway;

    private List<Client> clients;

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
        clients = clientGateway.readAll();
        lazyModel = new LazyDataModel<Client>() {
            @Override
            public int getRowCount() {
                return clientGateway.readAll().size();
            }

            @Override
            public List<Client> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return clients.stream()
                        .skip(first)
                        .filter(client -> client.getFullName().contains((String) filters.getOrDefault("fullName", "")))
                        .collect(Collectors.toList());
            }

            @Override
            public Client getRowData(String rowKey) {
                return clientGateway.read(rowKey);
            }

            @Override
            public Integer getRowKey(Client client) {
                return client.getId();
            }
        };

    }

    public LazyDataModel<Client> getLazyModel() {
        return lazyModel;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public void onRowSelect(SelectEvent event) {
    }
}
