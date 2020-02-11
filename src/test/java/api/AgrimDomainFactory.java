package api;

import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.entity.builder.VehicleBuilder;

import java.time.LocalDate;

public class AgrimDomainFactory {

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
