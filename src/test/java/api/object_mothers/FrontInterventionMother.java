package api.object_mothers;

import front.dtos.Intervention;

import java.time.LocalDateTime;

public class FrontInterventionMother {

    public static final String FAKE_TITLE = "fakeTitle";
    public static final LocalDateTime START_TIME = LocalDateTime.now().minusHours(2);
    public static final LocalDateTime END_TIME = LocalDateTime.now().minusHours(1);
    public static final String VEHICLE_ID = "999";
    public static final String REPAIRING_PACK_ID = "999";

    public static Intervention create(String title, String interventionType, String vehicleId, String repairingPackId, LocalDateTime startTime, LocalDateTime endTime) {
        return Intervention.builder()
                .title(title)
                .interventionType(interventionType)
                .vehicleId(vehicleId)
                .repairingPackId(repairingPackId)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public static Intervention interventionDto() {
        return create(FAKE_TITLE, "REPAIR", VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

    public static Intervention withVehicle(String vehicleId) {
        Intervention intervention = create(FAKE_TITLE, "REPAIR", vehicleId, REPAIRING_PACK_ID, START_TIME, END_TIME);
        return intervention;
    }

    public static Intervention cafe() {
        return create(FAKE_TITLE, "CAFFE", null, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

    public static Intervention notFinished() {
        return create(FAKE_TITLE, "REPAIR", VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, null);
    }

    public static Intervention withInterventionType(String interventionType) {
        return create(FAKE_TITLE, interventionType, VEHICLE_ID, REPAIRING_PACK_ID, START_TIME, END_TIME);
    }

}
