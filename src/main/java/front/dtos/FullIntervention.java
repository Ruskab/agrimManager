package front.dtos;

import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;

public class FullIntervention {

    private InterventionDto interventionDto;
    private VehicleDto vehicleDto;
    private MechanicDto mechanicDto;

    public static FullIntervention of(MechanicDto mechanicDto, InterventionDto interventionDto, VehicleDto vehicleDto) {
        FullIntervention fullIntervention = new FullIntervention();
        fullIntervention.setMechanicDto(mechanicDto);
        fullIntervention.setVehicleDto(vehicleDto);
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
        return vehicleDto.getVehicleDataSheet();
    }

    public InterventionDto getInterventionDto() {
        return interventionDto;
    }

    public void setInterventionDto(InterventionDto interventionDto) {
        this.interventionDto = interventionDto;
    }

    public VehicleDto getVehicleDto() {
        return vehicleDto;
    }

    public void setVehicleDto(VehicleDto vehicleDto) {
        this.vehicleDto = vehicleDto;
    }

    public MechanicDto getMechanicDto() {
        return mechanicDto;
    }

    public void setMechanicDto(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }
}
