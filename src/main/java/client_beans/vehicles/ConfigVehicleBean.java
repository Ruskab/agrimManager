package client_beans.vehicles;

import api.dtos.VehicleDto;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import static client_beans.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class ConfigVehicleBean {

    private VehicleDto selectedVehicleDto;
    private VehicleGateway vehicleGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway(getAuthToken());
    }

    public void save() {
        try {
            vehicleGateway.update(selectedVehicleDto);
        } catch (IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "update vehicle"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "update vehicle"));

    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }
}
