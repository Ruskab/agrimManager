package client_beans.clients;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import client_beans.clients.ClientGateway;
import client_beans.vehicles.VehicleGateway;
import com.mysql.cj.util.StringUtils;
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
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CreateClientBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateClientBean.class);
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    private ClientDto client;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway();
        clientGateway = new ClientGateway();
        client = new ClientDto();
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public void create() {
        if (client == null) {
            LOGGER.error("Cliente vacio");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }

        String clientId = clientGateway.create(client);
        String message = StringUtils.isStrictlyNumeric(clientId) ? "Successful" : "Error";

        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "create client"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("globalMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "create client"));
        PrimeFaces.current().dialog().closeDynamic(null);
        resertWizard();
    }

    private void resertWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('basicInfoTab', false)");
        client = new ClientDto();
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }
}
