package front.dtos;

import api.dtos.InterventionDto;

public class FullIntervention {

    private InterventionDto interventionDto;
    private Vehicle vehicle;
    private Mechanic mechanic;

    public static FullIntervention of(Mechanic mechanic, InterventionDto interventionDto, Vehicle vehicleDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanic(mechanic);
        fullIntervention.setVehicle(vehicleDto);
        fullIntervention.setInterventionDto(interventionDto);
        return fullIntervention;
    }

    public static FullIntervention of(Mechanic mechanic, InterventionDto interventionDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanic(mechanic);
        fullIntervention.setInterventionDto(interventionDto);
        return fullIntervention;
    }

    public String interventionVehicle() {
        if (interventionDto.getVehicleId() == null) {
            return "-";
        }
        return vehicle.getVehicleDataSheet();
    }

    public InterventionDto getInterventionDto() {
        return interventionDto;
    }

    public void setInterventionDto(InterventionDto interventionDto) {
        this.interventionDto = interventionDto;
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
}
