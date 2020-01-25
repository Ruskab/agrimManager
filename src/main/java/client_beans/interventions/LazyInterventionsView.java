package client_beans.interventions;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import client_beans.mechanics.MechanicGateway;
import client_beans.vehicles.VehicleGateway;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static client_beans.util.SessionUtil.getAuthToken;
import static client_beans.util.SessionUtil.getSessionMechanic;

@ManagedBean(name = "lazyInterventionsView")
@ViewScoped
public class LazyInterventionsView implements Serializable {

    private static final String NOT_NAME = "NOT NAME";
    private LazyDataModel<InterventionDto> lazyModel;
    private InterventionDto selectedInterventionDto;
    private MechanicDto mechanic;
    private Map<String, String> vehicles = new HashMap<>();
    private String vehicleReference;

    private InterventionGateway interventionGateway;
    private VehicleGateway vehicleGateway;
    private MechanicGateway mechanicGateway;

    @PostConstruct
    public void init() {
        interventionGateway = new InterventionGateway(getAuthToken());
        vehicleGateway = new VehicleGateway(getAuthToken());
        mechanicGateway = new MechanicGateway(getAuthToken());
        mechanic = getSessionMechanic("mechanic");
        mechanic = mechanicGateway.read(Integer.toString(mechanic.getId()));

        List<Integer> intervetionIds = mechanic.getInterventionIds();
        intervetionIds.forEach(this::setVehicleInfo);
        lazyModel = new LazyDataModel<InterventionDto>() {

            @Override
            public int getRowCount() {
                return mechanic.getInterventionIds().size();
            }

            @Override
            public List<InterventionDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                List<InterventionDto> filtered = mechanic.getInterventionIds().stream()
                        .skip(first)
                        .map(id -> interventionGateway.read(Integer.toString(id)))
                        .filter(intervention -> doFilter(intervention, filters))
                        .collect(Collectors.toList());

                if (sortField == null) {
                    return filtered;
                }

                return sortRows(sortField, sortOrder, filtered);
            }

            @Override
            public InterventionDto getRowData(String rowKey) {
                return interventionGateway.read(rowKey);
            }

            @Override
            public Integer getRowKey(InterventionDto interventionDto) {
                return interventionDto.getId();
            }

            private List<InterventionDto> sortRows(String sortField, SortOrder sortOrder, List<InterventionDto> filtered) {
                Comparator<InterventionDto> startDateComparador = Comparator.comparing(InterventionDto::getStartTime);
                Comparator<InterventionDto> endDateComparador = Comparator.comparing(InterventionDto::getEndTime);

                switch (sortField) {
                    case "startTime":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? startDateComparador : startDateComparador.reversed()).collect(Collectors.toList());
                    case "endTime":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? endDateComparador : endDateComparador.reversed()).collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            }

            private boolean doFilter(InterventionDto interventionDto, Map<String, Object> filters) {
                if (filters == null || filters.isEmpty()) {
                    return true;
                }

                return containsSearchString(interventionDto.getInterventionType().name(), filters.get("state"))
                        && doFilterVehicleReference(interventionDto.getVehicleId(), filters.get("vehicleReference"))
                        && globalContainsSearch(interventionDto, filters.get("globalFilter"));
            }

            private boolean doFilterVehicleReference(String vehicleId, Object vehicleReference) {
                if (!(vehicleReference instanceof String)) {
                    return true;
                }
                return vehicles.getOrDefault(vehicleId, NOT_NAME).toLowerCase().contains(((String) vehicleReference).toLowerCase());
            }

            private boolean globalContainsSearch(InterventionDto intervention, Object key) {
                if (!(key instanceof String)) {
                    return true;
                }
                String searchExpresion = ((String) key).toLowerCase();
                return intervention.getTitle().toLowerCase().contains(searchExpresion)
                        || intervention.getInterventionType().name().toLowerCase().contains(searchExpresion)
                        || intervention.getStartTime().toString().toLowerCase().contains(searchExpresion)
                        || vehicles.getOrDefault(intervention.getVehicleId(), NOT_NAME).toLowerCase().contains(searchExpresion);
            }

            private boolean containsSearchString(String name, Object searchExpresion) {
                if (!(searchExpresion instanceof String)) {
                    return true;
                }
                return name.contains((String) searchExpresion);
            }
        };
    }

    private void setVehicleInfo(Integer id) {
        InterventionDto intervention = interventionGateway.read(Integer.toString(id));
        if (intervention.getVehicleId() != null) {
            vehicles.putIfAbsent(intervention.getVehicleId(), getVehicleReference(vehicleGateway.read(intervention.getVehicleId())));
        }
    }

    public String getVehicleReference(VehicleDto vehicleDto) {
        return String.format("%s - %s", vehicleDto.getBrand(), vehicleDto.getRegistrationPlate());
    }

    public LazyDataModel<InterventionDto> getLazyModel() {
        return lazyModel;
    }

    public InterventionDto getSelectedInterventionDto() {
        return selectedInterventionDto;
    }

    public void setSelectedInterventionDto(InterventionDto selectedInterventionDto) {
        this.selectedInterventionDto = selectedInterventionDto;
    }

    public void onRowSelect(SelectEvent event) {
        String vehicleId = ((InterventionDto) event.getObject()).getVehicleId();
        vehicleReference = getVehicles().get(vehicleId);
    }


    public Map<String, String> getVehicles() {
        return vehicles;
    }

    //
    public void setVehicles(Map<String, String> vehicles) {
        this.vehicles = vehicles;
    }

    public String getVehicleReference() {
        return vehicleReference;
    }

    public void setVehicleReference(String vehicleReference) {
        this.vehicleReference = vehicleReference;
    }
}

