package front.beans;

import front.dtos.Client;
import front.dtos.Vehicle;
import front.gateways.ClientGateway;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@ManagedBean
@ViewScoped
public class ConfigClientBean implements Serializable {

    private Client client;
    private List<Vehicle> vehicles;
    private ClientGateway clientGateway;

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
    }

    public void save() {
        try {
            clientGateway.update(client);
            sendFrontMessage("editMessages", SEVERITY_INFO, "Successful", "update client");
        } catch (IllegalStateException e) {
            sendFrontMessage("editMessages", SEVERITY_ERROR, "Error", "update client");
        }
    }

    public void searchClientVehicles() {
        vehicles = clientGateway.searchClientVehicles(client);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }


}