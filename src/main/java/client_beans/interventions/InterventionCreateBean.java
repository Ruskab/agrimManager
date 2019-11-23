package client_beans.interventions;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import api.entity.State;
import client_beans.mechanics.MechanicGateway;
import client_beans.vehicles.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ManagedBean
@ViewScoped
public class InterventionCreateBean {

    private static final Logger LOGGER = LogManager.getLogger(InterventionCreateBean.class);
    private VehicleDto selectedVehicle;
    private InterventionDto selectedIntervention;
    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;
    private List<VehicleDto> vehicles;
    private boolean skip;

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway();
        mechanicGateway = new MechanicGateway();
        selectedIntervention = new InterventionDto();
        vehicles = new VehicleGateway().readAll();
    }

    public void create() {
        MechanicDto mechanic = (MechanicDto) Faces.getSession().getAttribute("mechanic");
        validateSelection();
        selectedIntervention.setVehicleId(Integer.toString(selectedVehicle.getId()));
        selectedIntervention.setStartTime(LocalDateTime.now());
        selectedIntervention.setState(State.REPAIR);
        mechanicGateway.addIntervention(mechanic, selectedIntervention);

//        Faces.getSession().setAttribute("activeIntervention", interventionId);
        PrimeFaces.current().executeScript("PF('interventionCreateDialog').hide();");
        resetWizard();
    }

    private void validateSelection() {
        if (selectedIntervention == null) {
            LOGGER.error("Intervention empty");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Intervention empty", ""));
        }
        if (selectedVehicle == null) {
            LOGGER.error("Vehiculo no seleccionado");
            FacesContext.getCurrentInstance().addMessage("confirmMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vehicle not selected", ""));
        }
    }

    private void resetWizard() {
        PrimeFaces.current().executeScript("PF('createVehicleWizzard').loadStep('selectVehicleTab', false)");
        selectedIntervention = new InterventionDto();
        selectedVehicle = null;
    }

    public List<VehicleDto> completeVehicleDataSheet(String query) {
        return vehicles.stream().filter(vehicleDto -> vehicleDto.getVehicleDataSheet().toLowerCase().contains(query.toLowerCase())).collect(toList());
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

    public VehicleDto getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(VehicleDto selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public InterventionDto getSelectedIntervention() {
        return selectedIntervention;
    }

    public void setSelectedIntervention(InterventionDto selectedIntervention) {
        this.selectedIntervention = selectedIntervention;
    }
}
