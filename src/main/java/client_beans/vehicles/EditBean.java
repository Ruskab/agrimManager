package client_beans.vehicles;

import api.dtos.VehicleDto;
import client_beans.clients.ClientGateway;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class EditBean {

    private VehicleDto selectedVehicleDto;
    private VehicleGateway vehicleGateway;
    private String clientName;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway();
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void save() {
        Integer responseStatus = vehicleGateway.update(selectedVehicleDto);
        String message = responseStatus == 200 ? "Successful" : "Error";
        FacesMessage msg = new FacesMessage(message, "Al guardar el vehiculo");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
