package front.beans;

import front.dtos.Client;
import api.dtos.VehicleDto;
import front.gateways.ClientGateway;
import front.gateways.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class ConfigClientBean implements Serializable {

    private Client client;
    private List<VehicleDto> vehicles;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
    }

    public void save() {
        try {
            clientGateway.update(client);
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "update client"));
        }catch (IllegalStateException e){
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "update client"));
        }
    }

    public void searchClientVehicles() {
        vehicles = vehicleGateway.searchBy(client);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<VehicleDto> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDto> vehicles) {
        this.vehicles = vehicles;
    }


}