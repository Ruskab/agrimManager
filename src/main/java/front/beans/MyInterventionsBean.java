package front.beans;

import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import front.dtos.FullIntervention;
import front.gateways.InterventionGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean(name = "myInterventionsBean") //NOSONAR
@ViewScoped //NOSONAR
public class MyInterventionsBean implements Serializable {

    private List<FullIntervention> interventions;
    private Intervention selectedIntervention;
    private Map<String, String> vehicles = new HashMap<>();
    private String selectedVehicleReference;
    private VehicleGateway vehicleGateway;

    @ManagedProperty(value="#{sessionBean}") //NOSONAR
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        InterventionGateway interventionGateway = new InterventionGateway(getAuthToken());
        MechanicGateway mechanicGateway = new MechanicGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        Mechanic mechanic = mechanicGateway.read(Integer.toString(sessionBean.getMechanic().getId()));
        interventions = mechanic.getInterventionIds().stream()
                .map(Object::toString)
                .map(interventionGateway::read)
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

    public List<FullIntervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<FullIntervention> interventions) {
        this.interventions = interventions;
    }

    public Intervention getSelectedIntervention() {
        return selectedIntervention;
    }

    public void setSelectedIntervention(Intervention selectedIntervention) {
        this.selectedIntervention = selectedIntervention;
    }

    public Map<String, String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<String, String> vehicles) {
        this.vehicles = vehicles;
    }

    public String getSelectedVehicleReference() {
        return selectedVehicleReference;
    }

    public void setSelectedVehicleReference(String selectedVehicleReference) {
        this.selectedVehicleReference = selectedVehicleReference;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}

