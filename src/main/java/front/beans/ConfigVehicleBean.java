package front.beans;

import front.dtos.Vehicle;
import front.gateways.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
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
            sendFrontMessage("editMessages", SEVERITY_ERROR, "Error", "update vehicle");
            return;
        }
        sendFrontMessage("editMessages", SEVERITY_INFO, "Successful", "update vehicle");
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }
}
