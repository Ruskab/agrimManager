package api.dtos;

import java.util.List;

public class ClientVehiclesDto {

    private ClientDto clientDto;

    private List<Integer> vehicles;

    public ClientVehiclesDto(ClientDto clientDto, List<Integer> vehicles) {
        this.clientDto = clientDto;
        this.vehicles = vehicles;
    }

    public ClientDto getClientDto() {
        return clientDto;
    }

    public void setClientDto(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    public List<Integer> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Integer> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "ClientVehiclesDto{" +
                "clientDto=" + clientDto +
                ", vehicles=" + vehicles +
                '}';
    }
}
