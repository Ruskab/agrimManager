package front.beans;

import front.dtos.Vehicle;
import front.gateways.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class ConfigVehicleBean {

    private Vehicle selectedVehicle;
    private VehicleGateway vehicleGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway(getAuthToken());
    }

    public void save() {
        try {
            vehicleGateway.update(selectedVehicle);
        } catch (IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "update vehicle"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "update vehicle"));

    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }
}
