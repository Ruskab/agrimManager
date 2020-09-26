package front.beans;

import api.dtos.ClientDto;
import com.mysql.cj.util.StringUtils;
import front.gateways.ClientGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class CreateClientBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateClientBean.class);
    private ClientGateway clientGateway;
    private ClientDto client;

    @PostConstruct
    public void init() {
        clientGateway = new ClientGateway(getAuthToken());
        client = new ClientDto();
    }

    public void create() {
        if (client == null) {
            LOGGER.error("Cliente vacio");
            FacesContext.getCurrentInstance()
                    .addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }

        String clientId = clientGateway.create(client);
        String message = StringUtils.isStrictlyNumeric(clientId) ? "Successful" : "Error";

        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance()
                    .addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "create client"));
            return;
        }
        FacesContext.getCurrentInstance()
                .addMessage("globalMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "create client"));
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

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }
}
