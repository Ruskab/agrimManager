package front.dtos;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;

public class FullIntervention {

    private InterventionDto interventionDto;
    private Vehicle vehicle;
    private MechanicDto mechanicDto;

    public static FullIntervention of(MechanicDto mechanicDto, InterventionDto interventionDto, Vehicle vehicleDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanicDto(mechanicDto);
        fullIntervention.setVehicle(vehicleDto);
        fullIntervention.setInterventionDto(interventionDto);
        return fullIntervention;
    }

    public static FullIntervention of(MechanicDto mechanicDto, InterventionDto interventionDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanicDto(mechanicDto);
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

    public MechanicDto getMechanicDto() {
        return mechanicDto;
    }

    public void setMechanicDto(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }
}
