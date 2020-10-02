package front.beans;

import com.mysql.cj.util.StringUtils;
import front.dtos.Client;
import front.dtos.Vehicle;
import front.gateways.ClientGateway;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@ManagedBean
@ViewScoped
public class CreateVehicleBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateVehicleBean.class);
    private boolean skip;
    private Vehicle selectedVehicle;
    private Client selectedClient;
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway(getAuthToken());
        clientGateway = new ClientGateway(getAuthToken());
        selectedVehicle = new Vehicle();
    }

    public void create() {
        if (selectedClient == null) {
            LOGGER.error("Cliente vacio");
            sendFrontMessage("confirmMessages", SEVERITY_ERROR, "Client empty", "");
            return;
        }
        selectedVehicle.setClientId(Integer.toString(selectedClient.getId()));
        String vehicleId = vehicleGateway.create(selectedVehicle);
        String message = StringUtils.isStrictlyNumeric(vehicleId) ? "Successful" : "Error";

        if ("Error".equals(message)) {
            sendFrontMessage("confirmMessages", SEVERITY_ERROR, message, "create vehicle");
            return;
        }
        sendFrontMessage("globalMessages", SEVERITY_INFO, message, "create vehicle");
        PrimeFaces.current().executeScript("PF('vehicleCreateDialog').hide();");
        resetWizard();
    }

    public List<Client> searchClient(String query) {
        return clientGateway.searchBy(query);
    }

    private void resetWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('basicInfoTab', false)");
        selectedVehicle = new Vehicle();
        selectedClient = null;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }
}
