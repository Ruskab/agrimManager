package front.beans;

import api.dtos.ClientDto;
import front.gateways.ClientGateway;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean(name = "lazyClientsBean")
@ViewScoped
public class LazyClientsBean implements Serializable {

    private LazyDataModel<ClientDto> lazyModel;

    private ClientDto selectedClientDto;

    private ClientGateway clientGateway;

    private List<ClientDto> clientDtos;

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
        clientDtos = clientGateway.readAll();
        lazyModel = new LazyDataModel<ClientDto>() {
            @Override
            public int getRowCount() {
                return clientGateway.readAll().size();
            }

            @Override
            public List<ClientDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return clientDtos.stream()
                        .skip(first)
                        .filter(client -> client.getFullName().contains((String) filters.getOrDefault("fullName", "")))
                        .collect(Collectors.toList());
            }

            @Override
            public ClientDto getRowData(String rowKey) {
                return clientGateway.read(rowKey);
            }

            @Override
            public Integer getRowKey(ClientDto clientDto) {
                return clientDto.getId();
            }
        };

    }

    public LazyDataModel<ClientDto> getLazyModel() {
        return lazyModel;
    }

    public ClientDto getSelectedClientDto() {
        return selectedClientDto;
    }

    public void setSelectedClientDto(ClientDto selectedClientDto) {
        this.selectedClientDto = selectedClientDto;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Client Selected", ((ClientDto) event.getObject()).getFullName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
