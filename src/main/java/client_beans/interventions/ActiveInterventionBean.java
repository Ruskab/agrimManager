package client_beans.interventions;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import client_beans.mechanics.MechanicGateway;
import client_beans.vehicles.VehicleGateway;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.util.Optional;

import static client_beans.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class ActiveInterventionBean {

    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;

    private InterventionDto activeIntervention;
    private VehicleDto vehicle;

    public InterventionDto getActiveIntervention() {
        return activeIntervention;
    }

    public void setActiveIntervention(InterventionDto activeIntervention) {
        this.activeIntervention = activeIntervention;
    }

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway(getAuthToken());
        mechanicGateway = new MechanicGateway(getAuthToken());
        Optional<InterventionDto> optionalIntervention = Optional.empty();
        String interventionId = (String) Faces.getSession().getAttribute("activeIntervention");
        if (interventionId == null) {
            MechanicDto mechanic = (MechanicDto) Faces.getSession().getAttribute("mechanic");
            optionalIntervention = tryFindActiveIntervention(mechanic);
            activeIntervention = optionalIntervention.orElse(null);
        } else {
            activeIntervention = interventionGateway.read(interventionId);
        }
        optionalIntervention.ifPresent(intervention -> vehicle = new VehicleGateway(getAuthToken()).read(activeIntervention.getVehicleId()));
    }

    private Optional<InterventionDto> tryFindActiveIntervention(MechanicDto mechanic) {
        MechanicDto actualMechanic = mechanicGateway.read(Integer.toString(mechanic.getId()));
        return actualMechanic.getInterventionIds().stream()
                .map(integer -> Integer.toString(integer))
                .map(interventionGateway::read)
                .filter(InterventionDto::isActiveIntervention).findFirst();
    }

    public void finishActiveIntervention() {
        activeIntervention.setEndTime(LocalDateTime.now());
        try {
            interventionGateway.update(activeIntervention);
            activeIntervention = null;
        } catch (IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }
}
