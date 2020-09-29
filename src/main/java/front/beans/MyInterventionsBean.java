package front.beans;

import api.dtos.InterventionDto;
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

@ManagedBean(name = "myInterventionsBean")
@ViewScoped
public class MyInterventionsBean implements Serializable {

    private List<FullIntervention> interventions;
    private InterventionDto selectedInterventionDto;
    private Mechanic mechanic;
    private Map<String, String> vehicles = new HashMap<>();
    private String selectedVehicleReference;
    private InterventionGateway interventionGateway;
    private VehicleGateway vehicleGateway;
    private MechanicGateway mechanicGateway;

    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        interventionGateway = new InterventionGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanic = sessionBean.getMechanic();
        mechanic = sessionBean.getMechanic();
        mechanic = mechanicGateway.read(Integer.toString(mechanic.getId()));
        interventions = mechanic.getInterventionIds().stream()
                .map(Object::toString)
                .map(interventionGateway::read)
                .map(intervention -> mapToFullIntervention(intervention, mechanic))
                .collect(Collectors.toList());

    }

    private FullIntervention mapToFullIntervention(InterventionDto intervention, Mechanic mechanic) {
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

    public InterventionDto getSelectedInterventionDto() {
        return selectedInterventionDto;
    }

    public void setSelectedInterventionDto(InterventionDto selectedInterventionDto) {
        this.selectedInterventionDto = selectedInterventionDto;
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

