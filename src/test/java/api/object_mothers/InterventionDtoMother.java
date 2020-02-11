package api.object_mothers;

import api.dtos.InterventionDto;
import api.entity.InterventionType;

import java.time.LocalDateTime;

public class InterventionDtoMother {

    public static final String FAKE_TITLE = "fakeTitle";
    public static final LocalDateTime START_TIME = LocalDateTime.now().minusHours(2);
    public static final LocalDateTime END_TIME = LocalDateTime.now().minusHours(1);
    public static final String VEHICLE_ID = "999";
    public static final String REPAIRING_PACK_ID = "999";

    public static InterventionDto create(String title, InterventionType interventionType, String vehicleId, String repairingPackId, LocalDateTime startTime, LocalDateTime endTime) {
        return new InterventionDto(title, interventionType, vehicleId, repairingPackId, startTime, endTime);
    }

    public static InterventionDto interventionDto() {
        return create(FAKE_TITLE, InterventionType.REPAIR, VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

    public static InterventionDto withVehicle(String vehicleId) {
        InterventionDto intervention = create(FAKE_TITLE, InterventionType.REPAIR, vehicleId, REPAIRING_PACK_ID, START_TIME, END_TIME);
        return intervention;
    }

    public static InterventionDto cafe() {
        return create(FAKE_TITLE, InterventionType.CAFFE, null, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

    public static InterventionDto notFinished() {
        return create(FAKE_TITLE, InterventionType.REPAIR, VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, null);
    }

    public static InterventionDto withInterventionType(InterventionType interventionType) {
        return create(FAKE_TITLE, interventionType, VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

}
