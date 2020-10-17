package front.dtos;

import java.time.Duration;
import java.time.LocalDateTime;

public class FullIntervention {

    private Intervention intervention;
    private Vehicle vehicle;
    private Mechanic mechanic;

    public static FullIntervention of(Mechanic mechanic, Intervention intervention, Vehicle vehicleDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanic(mechanic);
        fullIntervention.setVehicle(vehicleDto);
        fullIntervention.setIntervention(intervention);
        return fullIntervention;
    }

    public static FullIntervention of(Mechanic mechanic, Intervention intervention) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanic(mechanic);
        fullIntervention.setIntervention(intervention);
        return fullIntervention;
    }

    public String interventionVehicle() {
        if (intervention.getVehicleId() == null) {
            return "-";
        }
        return vehicle.getVehicleDataSheet();
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public long getTimeSpentMinutes() {
        return getTimeSpent().toMinutes();
    }

    public long getTimeSpentHours() {
        return getTimeSpent().toHours();
    }

    private Duration getTimeSpent() {
        if (isActive()) {
            return Duration.between(getIntervention().getStartTime(), LocalDateTime.now());
        }
        return Duration.between(getIntervention().getStartTime(), getIntervention().getEndTime());
    }

    public boolean isActive() {
        return getIntervention().getEndTime() == null;
    }
}
