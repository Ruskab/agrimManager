package api;

import api.dtos.InterventionDto;
import api.entity.State;

import java.time.LocalDateTime;

public class AgrimDomainFactory {

    public static InterventionDto createInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", State.REPAIR, vehicleId, null,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

    public static InterventionDto createCaffeInterventionDto() {
        return new InterventionDto("Caffe", State.CAFFE, null, null,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

}
