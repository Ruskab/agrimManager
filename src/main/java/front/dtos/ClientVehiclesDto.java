package front.dtos;

import java.util.List;

public class ClientVehiclesDto {

    private Client client;

    private List<Integer> vehicles;

    public static ClientVehiclesDto create(Client client, List<Integer> vehicles) {
        return new ClientVehiclesDto(client, vehicles);
    }

    private ClientVehiclesDto(Client client, List<Integer> vehicles) {
        this.client = client;
        this.vehicles = vehicles;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
                "client=" + client +
                ", vehicles=" + vehicles +
                '}';
    }
}
