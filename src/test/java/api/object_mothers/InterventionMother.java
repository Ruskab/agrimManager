package api.object_mothers;

import api.entity.Intervention;
import api.entity.InterventionType;
import api.entity.Vehicle;

import java.time.LocalDateTime;

public class InterventionMother {


    public static final String FAKE_TITLE = "fakeTitle";
    public static final LocalDateTime START_TIME = LocalDateTime.now().minusHours(2);
    public static final LocalDateTime END_TIME = LocalDateTime.now().minusHours(1);

    public static Intervention create(String title, InterventionType interventionType, LocalDateTime startTIme, LocalDateTime endTime) {
        return new Intervention(title, interventionType, startTIme, endTime);
    }

    public static Intervention intervention() {
        return create(FAKE_TITLE, InterventionType.REPAIR, START_TIME, END_TIME);
    }

    public static Intervention withVehicle(Vehicle vehicle) {
        Intervention intervention = create(FAKE_TITLE, InterventionType.REPAIR, START_TIME, END_TIME);
        intervention.setVehicle(vehicle);
        return intervention;
    }

    public static Intervention notFinished() {
        return create(FAKE_TITLE, InterventionType.REPAIR, START_TIME, null);
    }

    public static Intervention withInterventionType(InterventionType interventionType) {
        return create(FAKE_TITLE, interventionType, START_TIME, END_TIME);
    }

}
