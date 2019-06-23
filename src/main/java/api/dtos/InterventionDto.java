package api.dtos;

import api.entity.Intervention;

import java.time.Period;

public class InterventionDto {
    private int id;

    private String title;

    private Enum state;

    private Period period;

    private String vehicleId;

    private String repairingPackId;

    public InterventionDto(String title, Enum state, String vehicleId, String repairingPackId, Period period) {
        this.title = title;
        this.state = state;
        //todo si es de caffe no deberia de tener vehicle
        this.vehicleId = vehicleId;
        this.repairingPackId = repairingPackId;
        this.period = period;
    }

    public InterventionDto(Intervention intervention) {
        this.id = intervention.getId();
        this.title = intervention.getTitle();
        this.state = intervention.getState();
        intervention.getVehicle().ifPresent(vehicle -> this.vehicleId = Integer.toString(vehicle.getId()));
        intervention.getRepairingPack().ifPresent(repairingPack -> this.repairingPackId = Integer.toString(repairingPack.getId()));
        this.period = intervention.getPeriod();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Enum getState() {
        return state;
    }

    public void setState(Enum state) {
        this.state = state;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRepairingPackId() {
        return repairingPackId;
    }

    public void setRepairingPackId(String repairingPackId) {
        this.repairingPackId = repairingPackId;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "InterventionDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", vehicleId=" + vehicleId +
                ", repairingPackId=" + repairingPackId +
                '}';
    }
}
