package front.beans;

import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import front.gateways.InterventionGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@ManagedBean
@ViewScoped
public class DashboardBean {

    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;
    private VehicleGateway vehicleGateway;
    private Mechanic mechanic;

    private List<Intervention> activeInterventions = new ArrayList<>();

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway(getAuthToken());
        mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanic = sessionBean.getMechanic();
        activeInterventions = searchActiveInterventions(mechanic);
    }

    private List<Intervention> searchActiveInterventions(Mechanic mechanic) {
        return mechanicGateway.searchInterventionsByFilter(Integer.toString(mechanic.getId()), true);
    }

    public Vehicle searchVehicle(String vehicleId) {
        return vehicleGateway.read(vehicleId);
    }

    public void finishActiveIntervention(Intervention intervention) {
        try {
            mechanicGateway.finishIntervention(mechanic, intervention);
        } catch (IllegalStateException e) {
            sendFrontMessage("msg", SEVERITY_ERROR, "Client empty", "");
        }
        long duration = Duration.between(intervention.getStartTime(), LocalDateTime.now()).toHours();
        sendFrontMessage("msg", SEVERITY_INFO, "Intervention Finished Time: " + duration, "");
        activeInterventions = searchActiveInterventions(mechanic);
    }

    public List<Intervention> getActiveInterventions() {
        return activeInterventions;
    }

    public void setActiveInterventions(List<Intervention> activeInterventions) {
        this.activeInterventions = activeInterventions;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
