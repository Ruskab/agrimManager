package client_beans.clients;

import api.dtos.VehicleDto;
import client_beans.vehicles.VehicleGateway;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean(name = "lazyVehiclesView")
@ViewScoped
public class LazyVehiclesView implements Serializable {

    final private static String NOT_NAME = "NOT NAME";
    private LazyDataModel<VehicleDto> lazyModel;
    private VehicleDto selectedVehicleDto;
    private String clientName;
    private Map<String, String> clientNames = new HashMap<>();
    private VehicleGateway vehicleGateway = new VehicleGateway();
    private ClientGateway clientGateway = new ClientGateway();

    @PostConstruct
    public void init() {
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
                        .filter(client -> client.getBrand().contains((String) filters.getOrDefault("brand", "")))
                        .collect(Collectors.toList());

                if (sortField == null) {
                    return filtered;
                }

                return sortRows(sortField, sortOrder, filtered);
            }

            private List<VehicleDto> sortRows(String sortField, SortOrder sortOrder, List<VehicleDto> filtered) {
                Comparator<VehicleDto> registrationPlateComparator = Comparator.comparing(VehicleDto::getRegistrationPlate);
                Comparator<VehicleDto> brandComparator = Comparator.comparing(VehicleDto::getBrand);
                Comparator<VehicleDto> bodyOnFrameComparator = Comparator.comparing(VehicleDto::getBodyOnFrame);

                switch (sortField) {
                    case "registrationPlate":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? registrationPlateComparator : registrationPlateComparator.reversed()).collect(Collectors.toList());
                    case "brand":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? brandComparator : brandComparator.reversed()).collect(Collectors.toList());
                    case "bodyOnFrame":
                        return filtered.stream().sorted(sortOrder == SortOrder.ASCENDING ? bodyOnFrameComparator : bodyOnFrameComparator.reversed()).collect(Collectors.toList());
                    default:
                        return null;
                }
            }

            private boolean doFilter(VehicleDto vehicleDto, Map<String, Object> filters) {
                if (filters == null || filters.isEmpty()) {
                    return true;
                }

                return containsSearchString(vehicleDto.getRegistrationPlate(), filters.get("registrationPlate"))
                        && containsSearchString(vehicleDto.getBrand(), filters.get("brand"))
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

            @Override
            public Integer getRowKey(VehicleDto vehicleDto) {
                return vehicleDto.getId();
            }

            @Override
            public VehicleDto getRowData(String rowKey) {
                return vehicleGateway.read(rowKey);
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

    public String getClientDto(String clientId) {
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

