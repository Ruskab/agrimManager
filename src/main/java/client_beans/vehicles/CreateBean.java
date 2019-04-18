package client_beans.vehicles;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import client_beans.clients.ClientGateway;
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
        //todo añadir mensaje vehiculo guardado
        if (selectedClient != null){
            selectedVehicleDto.setClientId(Integer.toString(selectedClient.getId()));
        }
        String vehicleId = vehicleGateway.create(selectedVehicleDto);
        String message = vehicleId != "0" ? "Successful" : "Error";
        FacesMessage msg = new FacesMessage(message, "Vehicle created" + vehicleId);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        if("".equals(vehicleId))
            PrimeFaces.current().ajax().update("createDialogMsgs");
        else
            PrimeFaces.current().ajax().update("createDialogMsgs");

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
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }


}
