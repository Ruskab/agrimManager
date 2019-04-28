package client_beans.clients;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import client_beans.vehicles.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class ClientInfoBean implements Serializable {

    private ClientDto clientDto;
    private List<VehicleDto> vehicles;
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway = new ClientGateway();

    @PostConstruct
    public void init()
    {
        String clientId = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("parameters");
        clientDto = new ClientGateway().read(clientId);
        //todo hacer que se recupere el listado de vehiculos del cliente con una peticion concreta
        vehicles = new VehicleGateway().readAll();
        vehicles = vehicles.stream().filter(vehicle -> vehicle.getClientId().equals(Integer.toString(clientDto.getId()))).collect(Collectors.toList());
    }

    public void save() {
        Integer responseStatus = clientGateway.update(clientDto);
        String message = responseStatus == 200 ? "Successful" : "Error";
        if ("Error".equals(message)) {
            FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "update client"));
            return;
        }
        FacesContext.getCurrentInstance().addMessage("editMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, message, "update client"));

    }

    public ClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    public List<VehicleDto> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDto> vehicles) {
        this.vehicles = vehicles;
    }


}