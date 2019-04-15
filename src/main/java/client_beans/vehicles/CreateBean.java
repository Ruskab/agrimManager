package client_beans.vehicles;

import api.dtos.VehicleDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class CreateBean {

    private static final Logger LOGGER = LogManager.getLogger(CreateBean.class);
    private boolean skip;
    private VehicleDto selectedVehicleDto;
    private VehicleGateway vehicleGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway();
        selectedVehicleDto = new VehicleDto();
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public void create() {
        String vehicleId = vehicleGateway.create(selectedVehicleDto);
        String message = vehicleId != "0" ? "Successful" : "Error";
        FacesMessage msg = new FacesMessage(message, "Vehicle created" + vehicleId);
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
