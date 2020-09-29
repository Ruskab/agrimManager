package api;

import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;

import java.time.LocalDate;

public class AgrimBackEndDomainFactory {

    public static VehicleDto createVehicleDto() {
        return new VehicleDtoBuilder().byDefault().setClientId(VehicleDtoBuilder.CLIENT_ID).createVehicleDto();
    }

    public static VehicleDto createVehicleDto(String clientId) {
        return new VehicleDto("AA1234BB", clientId);

    }

    public static RepairingPackDto createRepairingPackDto() {
        return new RepairingPackDto(LocalDate.now(), 10);
    }


}
