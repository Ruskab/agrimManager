package client_beans.interventions;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import client_beans.mechanics.MechanicGateway;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.time.LocalDateTime;

@ManagedBean
@ViewScoped
public class ActiveInterventionBean {

    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;

    private InterventionDto activeIntervention;

    public InterventionDto getActiveIntervention() {
        return activeIntervention;
    }

    public void setActiveIntervention(InterventionDto activeIntervention) {
        this.activeIntervention = activeIntervention;
    }

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway();
        mechanicGateway = new MechanicGateway();
        String interventionId = (String) Faces.getSession().getAttribute("activeIntervention");
        if (interventionId == null) {
            MechanicDto mechanic = (MechanicDto) Faces.getSession().getAttribute("mechanic");
            activeIntervention = tryFindActiveIntervention(mechanic);
        } else {
            activeIntervention = interventionGateway.read(interventionId);
        }
    }

    private InterventionDto tryFindActiveIntervention(MechanicDto mechanic) {
        MechanicDto actualMechanic = mechanicGateway.read(Integer.toString(mechanic.getId()));
        return actualMechanic.getInterventionIds().stream()
                .map(integer -> Integer.toString(integer))
                .map(interventionGateway::read)
                .filter(InterventionDto::isActiveIntervention)
                .findFirst().orElse(null);
    }

    public void finishActiveIntervention(){
        activeIntervention.setEndTime(LocalDateTime.now());
        interventionGateway.update(activeIntervention);
    }

}
