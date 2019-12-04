package client_beans.vehicles;

import api.dtos.VehicleDto;

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

    public void save() {
        Integer responseStatus = vehicleGateway.update(selectedVehicleDto);
        String message = responseStatus == 200 ? "Successful" : "Error";
        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "update vehicle"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "update vehicle"));

    }
}
