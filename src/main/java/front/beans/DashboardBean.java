package front.beans;

import front.dtos.FullIntervention;
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
import java.util.stream.Collectors;

import static front.util.FrontMessages.sendFrontMessage;
import static front.util.SessionUtil.getAuthToken;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

@ManagedBean //NOSONAR
@ViewScoped //NOSONAR
public class DashboardBean {

    private MechanicGateway mechanicGateway;
    private VehicleGateway vehicleGateway;
    private Mechanic mechanic;

    private List<Intervention> activeInterventions = new ArrayList<>();
    private List<FullIntervention> activeFullInterventions = new ArrayList<>();

    @ManagedProperty(value = "#{sessionBean}") //NOSONAR
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanic = sessionBean.getMechanic();
        activeFullInterventions = searchActiveInterventions(mechanic).stream()
                .map(intervention -> mapToFullIntervention(intervention, mechanic))
                .collect(Collectors.toList());
    }

    private FullIntervention mapToFullIntervention(Intervention intervention, Mechanic mechanic) {
        if (intervention.getVehicleId() != null) {
            Vehicle vehicle = vehicleGateway.read(intervention.getVehicleId());
            return FullIntervention.of(mechanic, intervention, vehicle);
        }
        return FullIntervention.of(mechanic, intervention);
    }

    private List<Intervention> searchActiveInterventions(Mechanic mechanic) {
        return mechanicGateway.searchInterventionsByFilter(Integer.toString(mechanic.getId()), true);
    }

    private List<FullIntervention> searchFullActiveInterventions(Mechanic mechanic) {
        return searchActiveInterventions(mechanic).stream()
                .map(intervention -> mapToFullIntervention(intervention, mechanic))
                .collect(Collectors.toList());
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
        activeFullInterventions = searchFullActiveInterventions(mechanic);
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<FullIntervention> getActiveFullInterventions() {
        return activeFullInterventions;
    }

    public void setActiveFullInterventions(List<FullIntervention> activeFullInterventions) {
        this.activeFullInterventions = activeFullInterventions;
    }
}
