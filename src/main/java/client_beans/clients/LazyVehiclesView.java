package client_beans.clients;

import api.api_controllers.ClientApiController;
import api.api_controllers.VehicleApiController;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import client_beans.vehicles.VehicleGateway;
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

@ManagedBean(name = "lazyVehiclesView")
@ViewScoped
public class LazyVehiclesView implements Serializable {

    private LazyDataModel<VehicleDto> lazyModel;

    private VehicleDto selectedVehicleDto;
    private String clientName;
    private VehicleGateway vehicleGateway = new VehicleGateway();
    private ClientGateway clientGateway = new ClientGateway();

    @PostConstruct
    public void init() {
        lazyModel = new LazyDataModel<VehicleDto>() {
            @Override
            public int getRowCount() {
                return vehicleGateway.readAll().size();
            }

            @Override
            public List<VehicleDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return vehicleGateway.readAll().stream()
                        .skip(first)
                        .filter(client -> client.getBrand().contains((String) filters.getOrDefault("brand", "")))
                        .collect(Collectors.toList());
            }

            @Override
            public Integer getRowKey(VehicleDto vehicleDto) {
                return vehicleDto.getId();
            }

            @Override
            public VehicleDto getRowData(String rowKey) {
                return vehicleGateway.read(rowKey);
            }
        };

    }

    public LazyDataModel<VehicleDto> getLazyModel() {
        return lazyModel;
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public String getClientDto(String clientId){
        return clientGateway.read(clientId).getFullName();
    }


    public void onRowSelect(SelectEvent event) {
        String clientId = ((VehicleDto) event.getObject()).getClientId();
        clientName = clientGateway.read(clientId).getFullName();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
