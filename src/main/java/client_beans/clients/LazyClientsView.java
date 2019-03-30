package client_beans.clients;

import api.api_controllers.ClientApiController;
import api.dtos.ClientDto;
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

@ManagedBean(name = "lazyClientsView")
@ViewScoped
public class LazyClientsView implements Serializable {

    private LazyDataModel<ClientDto> lazyModel;

    private ClientDto selectedClientDto;

    private ClientApiController clientApiController = new ClientApiController();

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<ClientDto>() {
            @Override
            public int getRowCount() {
                return clientApiController.readAll().size();
            }

            @Override
            public List<ClientDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return clientApiController.readAll();
            }

            @Override
            public Integer getRowKey(ClientDto clientDto) {
                return clientDto.getId();
            }

            @Override
            public ClientDto getRowData(String rowKey) {
                return clientApiController.read(rowKey);
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

    public void setService(ClientApiController clientApiController) {
        this.clientApiController = clientApiController;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Client Selected", ((ClientDto) event.getObject()).getFullName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
