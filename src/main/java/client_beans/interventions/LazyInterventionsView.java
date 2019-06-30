package client_beans.interventions;

import api.dtos.InterventionDto;
import api.dtos.VehicleDto;
import client_beans.clients.ClientGateway;
import client_beans.vehicles.VehicleGateway;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean(name = "lazyInterventionsView")
@ViewScoped
public class LazyInterventionsView implements Serializable {

    private static final String NOT_NAME = "NOT NAME";
    private LazyDataModel<InterventionDto> lazyModel;
    private InterventionDto selectedInterventionDto;
    private InterventionGateway interventionGateway = new InterventionGateway();
    private VehicleGateway vehicleGateway = new VehicleGateway();
    private ClientGateway clientGateway = new ClientGateway();

    @PostConstruct
    public void init() {
        List<InterventionDto> intervetions = interventionGateway.readAll();
//        intervetions.forEach(interventionDto -> clientNames.putIfAbsent(vehicle.getClientId(), clientGateway.read(vehicle.getClientId()).getFullName()));
        lazyModel = new LazyDataModel<InterventionDto>() {

            @Override
            public int getRowCount() {
                return interventionGateway.readAll().size();
            }

            @Override
            public List<InterventionDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                List<InterventionDto> filtered = interventionGateway.readAll().stream()
                        .skip(first)
                        //.filter(vehicleDto -> doFilter(vehicleDto, filters))
                        //.filter(client -> client.getBrand().contains((String) filters.getOrDefault("brand", "")))
                        .collect(Collectors.toList());

                if (sortField == null) {
                    return filtered;
                }

                return sortRows(sortField, sortOrder, filtered);
            }

            private List<InterventionDto> sortRows(String sortField, SortOrder sortOrder, List<InterventionDto> filtered) {
                Comparator<InterventionDto> stateComparator = Comparator.comparing(InterventionDto::getState);
                Comparator<InterventionDto> titleComparator = Comparator.comparing(InterventionDto::getTitle);

                switch (sortField) {
                    case "state":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? titleComparator : titleComparator.reversed()).collect(Collectors.toList());
                    case "title":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? titleComparator : titleComparator.reversed()).collect(Collectors.toList());
                    default:
                        return null;
                }
            }

//            private boolean doFilter(VehicleDto vehicleDto, Map<String, Object> filters) {
//                if (filters == null || filters.isEmpty()) {
//                    return true;
//                }
//
//                return containsSearchString(vehicleDto.getRegistrationPlate(), filters.get("registrationPlate"))
//                        && containsSearchString(vehicleDto.getBodyOnFrame(), filters.get("bodyOnFrame"))
//                        && doFilterClientName(vehicleDto.getClientId(), filters.get("clientName"))
//                        && globalContainsSearch(vehicleDto, filters.get("globalFilter"));
//            }
//
//            private boolean doFilterClientName(String clientId, Object clientName) {
//                if (!(clientName instanceof String)) {
//                    return true;
//                }
//
//                return clientNames.getOrDefault(clientId, NOT_NAME).toLowerCase().contains(((String) clientName).toLowerCase());
//            }

//            private boolean globalContainsSearch(VehicleDto vehicle, Object key) {
//                if (!(key instanceof String)) {
//                    return true;
//                }
//                String searchExpresion = ((String) key).toLowerCase();
//                return vehicle.getRegistrationPlate().toLowerCase().contains(searchExpresion)
//                        || vehicle.getBrand().toLowerCase().contains(searchExpresion)
//                        || vehicle.getBodyOnFrame().toLowerCase().contains(searchExpresion)
//                        || clientNames.getOrDefault(vehicle.getClientId(), NOT_NAME).toLowerCase().contains(searchExpresion);
//            }

            private boolean containsSearchString(String name, Object searchExpresion) {
                if (!(searchExpresion instanceof String)) {
                    return true;
                }
                return name.contains((String) searchExpresion);
            }

            @Override
            public Integer getRowKey(InterventionDto interventionDto) {
                return interventionDto.getId();
            }

            @Override
            public InterventionDto getRowData(String rowKey) {
                return interventionGateway.read(rowKey);
            }
        };
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

//    public String getClientDto(String clientId) {
//        return getClientNames().getOrDefault(clientId, NOT_NAME);
//    }


    public void onRowSelect(SelectEvent event) {
        String clientId = ((VehicleDto) event.getObject()).getClientId();
    //    clientName = getClientNames().get(clientId);
    }

//    public String getClientName() {
//        return clientName;
//    }

//    public void setClientName(String clientName) {
//        this.clientName = clientName;
//    }

//    public Map<String, String> getClientNames() {
//        return clientNames;
//    }
//
//    public void setClientNames(Map<String, String> clientNames) {
//        this.clientNames = clientNames;
//    }
}

