package api.dtos;

import api.entity.Intervention;

import java.time.Period;

public class InterventionDto {
    private int id;

    private String title;

    private Enum state;

    private Period period;

    private String vehicleId;

    private String workId;

    public InterventionDto(String title, Enum state, String vehicleId, String workId, Period period) {
        this.title = title;
        this.state = state;
        this.vehicleId = vehicleId;
        this.workId = workId;
        this.period = period;
    }

    public InterventionDto(Intervention intervention) {
        this.id = intervention.getId();
        this.title = intervention.getTitle();
        this.state = intervention.getState();
        intervention.getVehicle().ifPresent(vehicle -> this.vehicleId = Integer.toString(vehicle.getId()));
        //this.workId = Integer.toString(intervention.getRepairingPack());
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

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
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
                ", workId=" + workId +
                '}';
    }
}
