package client_beans.clients;

import api.business_controllers.ClientBusinessController;
import api.dtos.ClientDto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;
import java.util.Map;

@ViewScoped
public class ClientsBean {

    private LazyDataModel model;

    @ManagedProperty("#{carService}")
    private ClientBusinessController clientBusinessController;

    public ClientsBean() {
        model = new LazyDataModel<ClientDto>() {
            @Override
            public List<ClientDto> load(int first, int pageSize, String  sortField,
                                        SortOrder sortOrder, Map<String,Object> filters) {
                List<ClientDto> clientDtos;
                clientDtos = clientBusinessController.readAll();
                model.setRowCount(clientDtos.size());
                return clientDtos;
            }
        };
    }

    public LazyDataModel getModel() {
        return model;
    }
}