package front.beans;

import front.dtos.FullIntervention;
import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import front.gateways.InterventionGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static front.util.SessionUtil.getAuthToken;

@ManagedBean(name = "lazyAllInterventionsBean")
@ViewScoped
public class LazyAllInterventionsBean implements Serializable {

    private LazyDataModel<FullIntervention> lazyModel;
    private Intervention selectedIntervention;
    private List<Mechanic> mechanics;
    private Mechanic user;
    private List<FullIntervention> fullInterventions = new ArrayList<>();
    private String selectedVehicleReference;
    private InterventionGateway interventionGateway;
    private VehicleGateway vehicleGateway;
    private MechanicGateway mechanicGateway;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        mechanicGateway = new MechanicGateway(getAuthToken());
        interventionGateway = new InterventionGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanics = mechanicGateway.listAll();
        loadFullInterventions(mechanics);
        user = sessionBean.getMechanic();
        lazyModel = new LazyDataModel<FullIntervention>() {

            @Override
            public int getRowCount() {
                return fullInterventions.size();
            }

            @Override
            public List<FullIntervention> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<FullIntervention> filtered = fullInterventions.stream()
                        .skip(first)
                        .filter(fullIntervention -> doFilter(fullIntervention, filters))
                        .collect(Collectors.toList());
                if (sortField == null) {
                    return filtered;
                }
                return sortRows(sortField, sortOrder, filtered);
            }

            @Override
            public FullIntervention getRowData(String rowKey) {
                return fullInterventions.stream()
                        .filter(i -> rowKey.equals(Integer.toString(i.getIntervention().getId())))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public Integer getRowKey(FullIntervention fullIntervention) {
                return fullIntervention.getIntervention().getId();
            }

            private List<FullIntervention> sortRows(String sortField, SortOrder sortOrder, List<FullIntervention> interventions) {
                Comparator<FullIntervention> startDateComparador = Comparator.comparing(interv -> interv.getIntervention()
                        .getStartTime());
                Comparator<FullIntervention> endDateComparador = Comparator.comparing(interv -> interv.getIntervention()
                        .getEndTime());
                switch (sortField) {
                    case "startTime":
                        return interventions.stream()
                                .sorted(sortOrder == SortOrder.ASCENDING ? startDateComparador : startDateComparador.reversed())
                                .collect(Collectors.toList());
                    case "endTime":
                        return interventions.stream()
                                .sorted(sortOrder == SortOrder.ASCENDING ? endDateComparador : endDateComparador.reversed())
                                .collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            }

            private boolean doFilter(FullIntervention intervention, Map<String, Object> filters) {
                if (filters == null || filters.isEmpty()) {
                    return true;
                }

                return containsSearchString(intervention.getIntervention()
                        .getInterventionType(), filters.get("state"))
                        && doFilterVehicleReference(intervention.getVehicle(), filters.get("interventionVehicle"))
                        && globalContainsSearch(intervention, filters.get("globalFilter"));
            }

            private boolean doFilterVehicleReference(Vehicle vehicle, Object vehicleReference) {
                if (!(vehicleReference instanceof String)) {
                    return true;
                }
                if (vehicle == null) {
                    return true;
                }
                return vehicle.getVehicleDataSheet().toLowerCase().contains(((String) vehicleReference).toLowerCase());
            }

            private boolean globalContainsSearch(FullIntervention fullIntervention, Object key) {
                Intervention intervention = fullIntervention.getIntervention();
                if (!(key instanceof String)) {
                    return true;
                }
                String searchExpresion = ((String) key).toLowerCase();
                return intervention.getTitle().toLowerCase().contains(searchExpresion)
                        || intervention.getInterventionType().toLowerCase().contains(searchExpresion)
                        || intervention.getStartTime().toString().toLowerCase().contains(searchExpresion)
                        || fullIntervention.getVehicle()
                        .getVehicleDataSheet()
                        .toLowerCase()
                        .contains(searchExpresion);
            }

            private boolean containsSearchString(String name, Object searchExpresion) {
                if (!(searchExpresion instanceof String)) {
                    return true;
                }
                return name.contains((String) searchExpresion);
            }
        };
    }

    private void loadFullInterventions(List<Mechanic> mechanics) {
        fullInterventions = mechanics.stream()
                .flatMap(mechanic -> toFullInterventionList(mechanic).stream())
                .collect(Collectors.toList());
    }

    private List<FullIntervention> toFullInterventionList(Mechanic mechanic) {
        return mechanic.getInterventionIds().stream()
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


    public String getVehicleReference(Vehicle vehicleDto) {
        return String.format("%s - %s", vehicleDto.getBrand(), vehicleDto.getRegistrationPlate());
    }

    public LazyDataModel<FullIntervention> getLazyModel() {
        return lazyModel;
    }

    public Intervention getSelectedIntervention() {
        return selectedIntervention;
    }

    public void setSelectedIntervention(Intervention selectedIntervention) {
        this.selectedIntervention = selectedIntervention;
    }

    public String getSelectedVehicleReference() {
        return selectedVehicleReference;
    }

    public void setSelectedVehicleReference(String selectedVehicleReference) {
        this.selectedVehicleReference = selectedVehicleReference;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}

