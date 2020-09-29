package front.beans;

import api.dtos.VehicleDto;
import front.dtos.Client;
import front.gateways.ClientGateway;
import com.mysql.cj.util.StringUtils;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class CreateVehicleBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateVehicleBean.class);
    private boolean skip;
    private VehicleDto selectedVehicleDto;
    private Client selectedClient;
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway(getAuthToken());
        clientGateway = new ClientGateway(getAuthToken());
        selectedVehicleDto = new VehicleDto();
    }

    public void create() {
        if (selectedClient == null) {
            LOGGER.error("Cliente vacio");
            FacesContext.getCurrentInstance()
                    .addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
            return;
        }
        selectedVehicleDto.setClientId(Integer.toString(selectedClient.getId()));
        String vehicleId = vehicleGateway.create(selectedVehicleDto);
        String message = StringUtils.isStrictlyNumeric(vehicleId) ? "Successful" : "Error";

        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance()
                    .addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "create vehicle"));
            return;
        }
        FacesContext.getCurrentInstance()
                .addMessage("globalMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "create vehicle"));
        PrimeFaces.current().executeScript("PF('vehicleCreateDialog').hide();");
        resertWizard();
    }

    public List<Client> searchClient(String query) {
        return clientGateway.searchBy(query);
    }

    private void resertWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('basicInfoTab', false)");
        selectedVehicleDto = new VehicleDto();
        selectedClient = null;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }
}
