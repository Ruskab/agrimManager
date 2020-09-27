package front.beans;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import front.gateways.InterventionGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean
@ViewScoped
public class DashboardBean {

    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;
    private VehicleGateway vehicleGateway;
    private MechanicDto mechanic;

    private List<InterventionDto> activeInterventions = new ArrayList<>();

    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway(getAuthToken());
        mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanic = sessionBean.getMechanicDto();
        activeInterventions = searchActiveInterventions(mechanic);
    }

    private List<InterventionDto> searchActiveInterventions(MechanicDto mechanic) {
        return mechanicGateway.searchInterventions(Integer.toString(mechanic.getId()), true);
    }

    public VehicleDto searchVehicle(String vehicleId) {
        return vehicleGateway.read(vehicleId);
    }

    public void finishActiveIntervention(InterventionDto intervention) {
        try {
            mechanicGateway.finishIntervention(mechanic, intervention);
        } catch (IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Client empty", ""));
        }
        long duration = Duration.between(intervention.getStartTime(), LocalDateTime.now()).toHours();
        FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Intervention Finished Time: " + duration, ""));
        activeInterventions =  searchActiveInterventions(mechanic);
    }

    public List<InterventionDto> getActiveInterventions() {
        return activeInterventions;
    }

    public void setActiveInterventions(List<InterventionDto> activeInterventions) {
        this.activeInterventions = activeInterventions;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
