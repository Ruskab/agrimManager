package front.beans;

import api.dtos.VehicleDto;
import front.gateways.ClientGateway;
import front.gateways.VehicleGateway;
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

import static front.util.SessionUtil.getAuthToken;

@ManagedBean(name = "lazyVehiclesBean")
@ViewScoped
public class LazyVehiclesBean implements Serializable {

    private static final String NOT_NAME = "NOT NAME";
    private static final String VEHICLE_BRAND = "brand";
    private LazyDataModel<VehicleDto> lazyModel;
    private VehicleDto selectedVehicleDto;
    private String clientName;
    private Map<String, String> clientNames = new HashMap<>();
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    @PostConstruct
    public void init() {
        vehicleGateway = new VehicleGateway(getAuthToken());
        clientGateway = new ClientGateway(getAuthToken());
        List<VehicleDto> vehicles = vehicleGateway.readAll();
        vehicles.forEach(vehicle -> clientNames.putIfAbsent(vehicle.getClientId(), clientGateway.read(vehicle.getClientId()).getFullName()));
        lazyModel = new LazyDataModel<VehicleDto>() {

            @Override
            public int getRowCount() {
                return vehicleGateway.readAll().size();
            }

            @Override
            public List<VehicleDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                List<VehicleDto> filtered = vehicleGateway.readAll().stream()
                        .skip(first)
                        .filter(vehicleDto -> doFilter(vehicleDto, filters))
                        .filter(vehicleDto -> vehicleDto.getBrand().contains((String) filters.getOrDefault(VEHICLE_BRAND, "")))
                        .collect(Collectors.toList());

                if (sortField == null) {
                    return filtered;
                }

                return sortRows(sortField, sortOrder, filtered);
            }

            @Override
            public VehicleDto getRowData(String rowKey) {
                return vehicleGateway.read(rowKey);
            }

            @Override
            public Integer getRowKey(VehicleDto vehicleDto) {
                return vehicleDto.getId();
            }

            private List<VehicleDto> sortRows(String sortField, SortOrder sortOrder, List<VehicleDto> filtered) {
                Comparator<VehicleDto> registrationPlateComparator = Comparator.comparing(VehicleDto::getRegistrationPlate);
                Comparator<VehicleDto> brandComparator = Comparator.comparing(VehicleDto::getBrand);
                Comparator<VehicleDto> bodyOnFrameComparator = Comparator.comparing(VehicleDto::getBodyOnFrame);

                switch (sortField) {
                    case "registrationPlate":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? registrationPlateComparator : registrationPlateComparator.reversed()).collect(Collectors.toList());
                    case VEHICLE_BRAND:
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? brandComparator : brandComparator.reversed()).collect(Collectors.toList());
                    case "bodyOnFrame":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? bodyOnFrameComparator : bodyOnFrameComparator.reversed()).collect(Collectors.toList());
                    default:
                        return Collections.emptyList();
                }
            }

            private boolean doFilter(VehicleDto vehicleDto, Map<String, Object> filters) {
                if (filters == null || filters.isEmpty()) {
                    return true;
                }

                return containsSearchString(vehicleDto.getRegistrationPlate(), filters.get("registrationPlate"))
                        && containsSearchString(vehicleDto.getBrand(), filters.get(VEHICLE_BRAND))
                        && containsSearchString(vehicleDto.getBodyOnFrame(), filters.get("bodyOnFrame"))
                        && doFilterClientName(vehicleDto.getClientId(), filters.get("clientName"))
                        && globalContainsSearch(vehicleDto, filters.get("globalFilter"));
            }

            private boolean doFilterClientName(String clientId, Object clientName) {
                if (!(clientName instanceof String)) {
                    return true;
                }

                return clientNames.getOrDefault(clientId, NOT_NAME).toLowerCase().contains(((String) clientName).toLowerCase());
            }

            private boolean globalContainsSearch(VehicleDto vehicle, Object key) {
                if (!(key instanceof String)) {
                    return true;
                }
                String searchExpresion = ((String) key).toLowerCase();
                return vehicle.getRegistrationPlate().toLowerCase().contains(searchExpresion)
                        || vehicle.getBrand().toLowerCase().contains(searchExpresion)
                        || vehicle.getBodyOnFrame().toLowerCase().contains(searchExpresion)
                        || clientNames.getOrDefault(vehicle.getClientId(), NOT_NAME).toLowerCase().contains(searchExpresion);
            }

            private boolean containsSearchString(String name, Object searchExpresion) {
                if (!(searchExpresion instanceof String)) {
                    return true;
                }
                return name.contains((String) searchExpresion);
            }
        };
    }

    public LazyDataModel<VehicleDto> getLazyModel() {
        return lazyModel;
    }

    public VehicleDto getSelectedVehicleDto() {
        return selectedVehicleDto;
    }

    public void setSelectedVehicleDto(VehicleDto selectedVehicleDto) {
        this.selectedVehicleDto = selectedVehicleDto;
    }

    public String getClient(String clientId) {
        return getClientNames().getOrDefault(clientId, NOT_NAME);
    }


    public void onRowSelect(SelectEvent event) {
        String clientId = ((VehicleDto) event.getObject()).getClientId();
        clientName = getClientNames().get(clientId);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Map<String, String> getClientNames() {
        return clientNames;
    }

    public void setClientNames(Map<String, String> clientNames) {
        this.clientNames = clientNames;
    }
}

