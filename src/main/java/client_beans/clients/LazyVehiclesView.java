package client_beans.clients;

import api.api_controllers.ClientApiController;
import api.api_controllers.VehicleApiController;
import api.dtos.ClientDto;
import api.dtos.VehicleDto;
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

    private VehicleApiController vehicleApiController = new VehicleApiController();

    private ClientApiController clientApiController = new ClientApiController();

    private List<VehicleDto> vehicleDtos;

    @PostConstruct
    public void init() {
        vehicleDtos = vehicleApiController.readAll();
        lazyModel = new LazyDataModel<VehicleDto>() {
            @Override
            public int getRowCount() {
                return vehicleApiController.readAll().size();
            }

            @Override
            public List<VehicleDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return vehicleDtos.stream()
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
                return vehicleApiController.read(rowKey);
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
        return clientApiController.read(clientId).getFullName();
    }

    public void setService(VehicleApiController vehicleApiController) {
        this.vehicleApiController = vehicleApiController;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Vehicle Selected", ((VehicleDto) event.getObject()).getRegistrationPlate());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
