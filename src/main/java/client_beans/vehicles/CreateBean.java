package client_beans.vehicles;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import client_beans.clients.ClientGateway;
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
public class CreateBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateBean.class);
    private boolean skip;
    private VehicleDto selectedVehicleDto;
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    private List<ClientDto> clientDtos;
    private ClientDto selectedClient;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway();
        clientGateway = new ClientGateway();
        selectedVehicleDto = new VehicleDto();
        clientDtos = clientGateway.readAll();
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public ClientDto getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(ClientDto selectedClient) {
        this.selectedClient = selectedClient;
    }

    public void create() {
        if (selectedClient == null) {
            LOGGER.error("Cliente vacio");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }
        selectedVehicleDto.setClientId(Integer.toString(selectedClient.getId()));

        String vehicleId = vehicleGateway.create(selectedVehicleDto);
        String message = StringUtils.isStrictlyNumeric(vehicleId) ? "Successful" : "Error";

        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "create vehicle"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("globalMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "create vehicle"));
        PrimeFaces.current().executeScript("PF('vehicleCreateDialog').hide();");
        resertWizard();
    }

    private void resertWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('basicInfoTab', false)");
        selectedVehicleDto = new VehicleDto();
        selectedClient = null;
    }

    public List<ClientDto> completeClient(String query) {
        List<ClientDto> tempList = clientDtos;
        return tempList.stream().filter(client -> client.getFullName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
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
}
