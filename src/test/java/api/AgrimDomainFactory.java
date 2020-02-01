package api;

import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.entity.Client;
import api.entity.InterventionType;
import api.entity.builder.VehicleBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AgrimDomainFactory {

    public static InterventionDto createInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", InterventionType.REPAIR, vehicleId, null,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

    public static InterventionDto createInterventionDto(String vehicleId, String repairingPackId) {
        return new InterventionDto("Reparacion", InterventionType.REPAIR, vehicleId, repairingPackId,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

    public static InterventionDto createCaffeInterventionDto() {
        return new InterventionDto("Caffe", InterventionType.CAFFE, null, null,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

    public static ClientDto createClientDto() {
        return new ClientDto("fake", 2);
    }

    public static VehicleDto createVehicle() {
        return new VehicleDto(new VehicleBuilder("AA1234BB").byDefault().createVehicle());
    }

    public static VehicleDto createVehicle(String clientId) {
        VehicleDto vehicleDto = new VehicleDto("AA1234BB", clientId);
        return vehicleDto;
    }

    public static RepairingPackDto createRepairingPackDto() {
        return new RepairingPackDto(LocalDate.now(), 10);
    }
}
